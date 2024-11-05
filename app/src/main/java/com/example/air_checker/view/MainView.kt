package com.example.air_checker.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
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
                    println("Failed to fetch location")
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

    LaunchedEffect(nearestStation) {
        nearestStation?.let { station ->
            airQualityIndexViewModel.fetchSensorsDataByStationId(station.id)
            Log.d("NearestStation", "ID: ${station.id}, " +
                "Distance: ${"%.2f".format(station.distanceTo)} m")
        }
    }

    Column() {
        Text(
            text = nearestStation?.let { station ->
                "Nearest station: ${station.id}, " +
                    "distance to: ${"%.2f".format(station.distanceTo)} m"
            } ?: "Brak najbliższej stacji"
        )

        // Wyświetlamy dane z sensorów
        Text(text = sensorData)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Text(text = "Podgląd tekstu najbliższej stacji")
}
