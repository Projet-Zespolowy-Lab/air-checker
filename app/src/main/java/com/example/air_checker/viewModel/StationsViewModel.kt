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

  // Pole do śledzenia błędu połączenia
  private val _networkError = MutableLiveData(false)
  val networkError: LiveData<Boolean> = _networkError

  // Funkcja aktualizująca stan błędu sieci
  fun setNetworkError(hasError: Boolean) {
    _networkError.postValue(hasError)
  }

  // Funkcja do pobrania stacji i obliczenia najbliższej
  fun fetchStations(userLat: Double, userLon: Double) {
    Log.d("StationsViewModel", "fetchStations called with coordinates: $userLat, $userLon")

    CoroutineScope(Dispatchers.IO).launch {
      getAllStations(
        onResult = { stations ->
          _stationsList.postValue(stations.listStations)
          Log.d("StationsViewModel", "Fetched stations count: ${stations.listStations.size}")

          _nearestStation.postValue(stations.nearestStation())
          setNetworkError(false)  // Reset błędu sieci po udanym pobraniu
        },
        onError = { error ->
          Log.e("StationsViewModel", "Error fetching stations: $error")
          setNetworkError(true)  // Ustawienie błędu sieci w przypadku błędu
        },
        userLat = userLat,
        userLon = userLon
      )
    }
  }
}
