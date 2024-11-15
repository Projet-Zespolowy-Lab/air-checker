package com.example.air_checker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.air_checker.model.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Coordinates(val latitude: Double, val longitude: Double)

class LocationViewModel : ViewModel() {
    private val locationModel = LocationModel()
    private val _coordinates = MutableStateFlow<Coordinates?>(null)
    val coordinates = _coordinates.asStateFlow()

    fun fetchLocation(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val location = locationModel.getGeolocation(apiKey)
            _coordinates.value = if (location != null) {
                Coordinates(location.first, location.second)
            } else {
                Log.e("fetchLocation", "Empty location")
                null
            }
        }
    }
}
