package com.example.air_checker.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.air_checker.BuildConfig
import com.example.air_checker.viewModel.StationsViewModel

class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Wywołanie fetchStations z podanymi współrzędnymi
        val userLat = 50.2648f
        val userLon = 19.0237f
        stationsViewModel.fetchStations(userLat, userLon)

        setContent {
            TextNearestStation(stationsViewModel = stationsViewModel)
        }

        LogSecretAPIKey()
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

    Text(
        text = nearestStation?.let { station ->
            "Nearest station: ${station.id}, distance to: ${station.distanceTo} m"
        } ?: "Brak najbliższej stacji",
        modifier = modifier
    )
}

fun LogSecretAPIKey(){//Funkcja demo - można usunąć
    Log.d("Secret", BuildConfig.API_KEY)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Text(text = "Podgląd tekstu najbliższej stacji")
}
