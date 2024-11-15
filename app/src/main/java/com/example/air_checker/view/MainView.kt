package com.example.air_checker.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiKey = BuildConfig.API_KEY

        // Obserwacja połączenia sieciowego
        observeNetworkConnectivity()

        // Regularne pobieranie lokalizacji i stacji co minutę
        lifecycleScope.launch {
            while (true) {
                if (isNetworkAvailable()) {
                    locationViewModel.fetchLocation(apiKey)
                    stationsViewModel.setNetworkError(false) // reset błędu sieci
                } else {
                    stationsViewModel.setNetworkError(true) // ustawienie błędu sieci
                    Log.d("MainActivity", "Brak połączenia z internetem")
                }
                delay(60000) // odświeżanie co minutę
            }
        }

        lifecycleScope.launch {
            locationViewModel.coordinates.collectLatest { coordinates ->
                if (coordinates != null && isNetworkAvailable()) {
                    stationsViewModel.fetchStations(coordinates.latitude, coordinates.longitude)
                    stationsViewModel.setNetworkError(false) // reset błędu sieci po udanym pobraniu
                } else {
                    stationsViewModel.setNetworkError(true)
                    Log.d("MainActivity", "Brak połączenia lub brak współrzędnych")
                }
            }
        }

        setContent {
            TextNearestStation(stationsViewModel = stationsViewModel)
        }
    }

    // Funkcja nasłuchująca stanu połączenia sieciowego
    private fun observeNetworkConnectivity() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                stationsViewModel.setNetworkError(false)
            }

            override fun onLost(network: Network) {
                stationsViewModel.setNetworkError(true)
            }
        })
    }

    // Funkcja sprawdzająca dostępność połączenia sieciowego (na żądanie)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

@Composable
fun TextNearestStation(stationsViewModel: StationsViewModel, modifier: Modifier = Modifier) {
    val nearestStation by stationsViewModel.nearestStation.observeAsState()
    val networkError by stationsViewModel.networkError.observeAsState(false)

    Column(modifier = modifier) {
        when {
            networkError -> {
                // Komunikat o braku internetu
                Text(text = "Brak połączenia z internetem. Nie można pobrać stacji.")
            }
            nearestStation != null -> {
                // Wyświetlenie informacji o najbliższej stacji
                Text(text = "Nearest station: ${nearestStation?.id}, distance to: ${nearestStation?.distanceTo} m")
            }
            else -> {
                // Tekst, gdy brak danych o najbliższej stacji
                Text(text = "Brak najbliższej stacji")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Text(text = "Podgląd tekstu najbliższej stacji")
}
