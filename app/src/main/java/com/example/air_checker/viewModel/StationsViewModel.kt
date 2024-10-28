package com.example.air_checker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.air_checker.model.Station
import com.example.air_checker.model.getAllStations

class StationsViewModel : ViewModel() {
  private val _nearestStation = MutableLiveData<Station?>()
  val nearestStation: LiveData<Station?> = _nearestStation

  private val _stationsList = MutableLiveData<List<Station>>()
  val stationsList: LiveData<List<Station>> = _stationsList

  // Funkcja do pobrania stacji i obliczenia najbliższej
  fun fetchStations(userLat: Float, userLon: Float) {
    Log.d("StationsViewModel", "fetchStations called with coordinates: $userLat, $userLon")

    CoroutineScope(Dispatchers.IO).launch {
      getAllStations(
        onResult = { stations ->
          _stationsList.postValue(stations.listStations)
          // Zaloguj liczbę stacji w listStations
          Log.d("StationsViewModel", "Fetched stations count: ${stations.listStations.size}")

          _nearestStation.postValue(stations.nearestStation())
        },
        onError = { error ->
          // Obsługa błędu - można zaktualizować LiveData dla błędów lub wyświetlić komunikat
          Log.e("StationsViewModel", "Error fetching stations: $error")
        },
        userLat = userLat,
        userLon = userLon
      )
    }
  }
}
