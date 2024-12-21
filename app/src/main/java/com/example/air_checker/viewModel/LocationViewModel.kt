package com.example.air_checker.viewModel

import androidx.lifecycle.ViewModel
import com.example.air_checker.model.LocationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LocationViewModel: ViewModel() {
    private val uiState = MutableStateFlow(LocationModel())
    val state: StateFlow<LocationModel> = uiState;

    fun update(latitude: Double, longitude: Double) {
        uiState.update { it.copy(latitude, longitude) }
    }
}
