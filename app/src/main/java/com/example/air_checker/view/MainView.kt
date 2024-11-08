package com.example.air_checker.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.air_checker.BuildConfig
import com.example.air_checker.R
import com.example.air_checker.viewModel.AirQualityIndexViewModel
import com.example.air_checker.viewModel.LocationViewModel
import com.example.air_checker.viewModel.StationsViewModel
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
    MainView()
/*
    Column() {
        Text(
            text = nearestStation?.let { station ->
                "Nearest station: ${station.id}, " +
                    "distance to: ${"%.2f".format(station.distanceTo)} m"
            } ?: "Brak najbliższej stacji"
        )

        // Wyświetlamy dane z sensorów
        Text(text = sensorData)
        // Wyświetlanie kategorii jakości powietrza
        airQualityCategories?.let { categories ->
            categories.airQualityCategories.forEach { category ->
                Text(text = "${category.categoryName}: ${category.qualityValue ?: "Brak danych"}")
            }
        }

    }
*/
}

@Composable
fun IndexField(indexName: String, indexValue: Int){
    Row(modifier = Modifier.fillMaxWidth().height(30.dp)) {
        Spacer(Modifier.width(10.dp))
        Image(
            painter = painterResource(R.drawable.info_blue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = indexName,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal))
        )
        Spacer(Modifier.weight(0.8f))
        Text(
            text = indexValue.toString(),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            modifier = Modifier.align(Alignment.CenterVertically).offset(x = (-10).dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainView() {
    Column(Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp)
        ){
            Image(
                painter = painterResource(R.drawable.info),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(31.dp, 31.dp)
            )
        }
        Spacer(Modifier.height(30.dp))
        Box(modifier = Modifier.fillMaxWidth().size(240.dp).drawBehind {
            drawCircle(
                color = Color(0xFF80E4FF),
                radius = 320f
            )
            drawCircle(
                color = Color(0xFFFFE9C9),
                radius = 305f
            )
        }) {
            Column(modifier = Modifier.align(Alignment.TopCenter)){
                Text(
                    text = "AIR METER",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                    modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = 30.dp).padding(top = 15.dp)
                )
                Text(
                    text = "29",
                    fontSize = 96.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Good",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                    modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = (-30).dp)
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(15.dp, 15.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Warsaw, PL",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            )
        }
        Spacer(Modifier.height(50.dp))
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            IndexField("PM 2.5", 30)
            IndexField("PM 10", 30)
            IndexField("NO 2", 2)
            IndexField("SO 2", -1)
        }
    }
}
