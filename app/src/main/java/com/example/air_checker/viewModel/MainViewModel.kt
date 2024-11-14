package com.example.air_checker.viewModel

import android.util.Log
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.Station
import com.example.air_checker.model.StationResponse

class MainViewModel {
    fun getNameNearestStation(nearestStation: Station?): String {
        nearestStation.let { station ->
            if (station != null) {
                return station.name
            }
        }
        return ""
    }

    fun getQuality(airQualityCategories: AirQualityCategories?, indexName: String): String {
        Log.d("cat", airQualityCategories?.airQualityCategories.toString())
        airQualityCategories.let { categories ->
            if (categories != null) {
                val categoryList = categories.airQualityCategories
                for (category in categoryList) {
                    if (category.categoryName == indexName) {
                        if (category.qualityValue != null) {
                            Log.d("test", category.qualityValue)
                            return category.qualityValue
                        }
                        return "Nieznany"
                    }
                }
            }
        }
        return "Nieznany"
    }
}