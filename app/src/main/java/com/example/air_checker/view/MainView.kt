package com.example.air_checker.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.air_checker.BuildConfig
import com.example.air_checker.viewModel.AirQualityIndexViewModel
import com.example.air_checker.viewModel.StationsViewModel
import com.example.air_checker.viewModel.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val airQualityIndexViewModel: AirQualityIndexViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiKey = BuildConfig.API_KEY
        locationViewModel.fetchLocation(apiKey)

        // Wywołanie fetchStations z podanymi współrzędnymi
        lifecycleScope.launch {
            locationViewModel.coordinates.collectLatest { coordinates ->
                if (coordinates != null) {
                    // Przekaż współrzędne do stationsViewModel
                    stationsViewModel.fetchStations(coordinates.latitude, coordinates.longitude)


                } else {
                    // Obsłuż błąd pobierania współrzędnych
                    Log.d("Error while fetching locations", "coordinates are null")
                }
            }
        }

        setContent {
            TextNearestStation(stationsViewModel = stationsViewModel,
                               airQualityIndexViewModel = airQualityIndexViewModel)
        }
    }
}

@Composable
fun TextNearestStation(
    stationsViewModel: StationsViewModel,
    airQualityIndexViewModel: AirQualityIndexViewModel
) {
    val nearestStation by stationsViewModel.nearestStation.observeAsState()
    val sensorData by airQualityIndexViewModel.sensorData.observeAsState(initial = "Ładowanie danych...")
    val airQualityCategories by airQualityIndexViewModel.airQualityCategories.observeAsState()

    LaunchedEffect(nearestStation) {
        nearestStation?.let { station ->
            airQualityIndexViewModel.fetchSensorsDataByStationId(station.id)
            Log.d("NearestStation", "ID: ${station.id}, " +
                "Distance: ${"%.2f".format(station.distanceTo)} m")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Odstęp wokół kafelka
            elevation = CardDefaults.cardElevation(8.dp) // Cień dla kafelka
    ) {
        Column() {
            Text(
                text = nearestStation?.let { station ->
                    "Nearest station: ${station.id}, " +
                        "Name: ${station.name}, " +
                        "distance to: ${"%.2f".format(station.distanceTo)} m"
                } ?: "Brak najbliższej stacji",
                modifier = Modifier
                    .fillMaxWidth()// Tekst zajmuje pełną szerokość dostępnego miejsca
                    .padding(top = 16.dp)
                    .padding(16.dp)         // Dodaje odstęp wokół tekstu
                    .background(Color.LightGray) // Dodaje tło w kolorze jasnoszarym
                    .padding(8.dp)          // Dodatkowy padding wewnątrz tła
            )

            // Wyświetlamy dane z sensorów
//        Text(text = sensorData)

            // Wyświetlanie kategorii jakości powietrza
            airQualityCategories?.let { categories ->
                categories.airQualityCategories.forEach { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp), // Odstęp między kafelkami
                        elevation = CardDefaults.cardElevation(4.dp) // Cień dla kafelka
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp) // Odstęp wewnątrz kafelka
                        ) {
                            Text(
                                text = category.categoryName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${category.qualityValue ?: "Brak danych"}",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp) // Odstęp między nazwą a wartością
                            )
                        }
                    }
                }
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Text(text = "Podgląd tekstu najbliższej stacji")
}
