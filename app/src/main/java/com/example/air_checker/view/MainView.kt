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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.air_checker.BuildConfig
import com.example.air_checker.viewModel.StationsViewModel
import com.example.air_checker.viewModel.LocationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

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
            TextNearestStation(stationsViewModel = stationsViewModel)
        }
    }
}

@Composable
fun TextNearestStation(stationsViewModel: StationsViewModel, modifier: Modifier = Modifier) {
    val nearestStation by stationsViewModel.nearestStation.observeAsState()

    LaunchedEffect(nearestStation) {
        nearestStation?.let { station ->
            Log.d("NearestStation", "ID: ${station.id}, Distance: ${station.distanceTo} m")
        }
    }

    Column(modifier = modifier) {
        Text(
            text = nearestStation?.let { station ->
                "Nearest station: ${station.id}, distance to: ${station.distanceTo} m"
            } ?: "Brak najbliższej stacji"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Text(text = "Podgląd tekstu najbliższej stacji")
}
