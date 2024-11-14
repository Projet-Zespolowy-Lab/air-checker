package com.example.air_checker.viewModel

import android.util.Log
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.IndexColors
import com.example.air_checker.model.Station

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
        airQualityCategories.let { categories ->
            if (categories != null) {
                val categoryList = categories.airQualityCategories
                for (category in categoryList) {
                    Log.d("cat", category.qualityValue.toString())
                    if (category.categoryName == indexName) {
                        if (category.qualityValue != null) {
                            return category.qualityValue
                        }
                        return "Nieznany"
                    }
                }
            }
        }
        return "Nieznany"
    }
    fun getColor(indexValue: String): Long {
        when(indexValue){
            "Bardzo dobry" -> return IndexColors.Bardzo_Dobry.rgb
            "Dobry" -> return IndexColors.Dobry.rgb
            "Umiarkowany" -> return IndexColors.Umiarkowany.rgb
            "Dostateczny" -> return IndexColors.Dostateczny.rgb
            "Zły" -> return IndexColors.Zły.rgb
            "Bardzo zły" -> return IndexColors.Bardzo_Zły.rgb
        }
        return IndexColors.Brak.rgb
    }
    fun getPercentageAirPurity(indexValue: String): String {
        when(indexValue){
            "Bardzo dobry" -> return IndexColors.Bardzo_Dobry.value
            "Dobry" -> return IndexColors.Dobry.value
            "Umiarkowany" -> return IndexColors.Umiarkowany.value
            "Dostateczny" -> return IndexColors.Dostateczny.value
            "Zły" -> return IndexColors.Zły.value
            "Bardzo zły" -> return IndexColors.Bardzo_Zły.value
        }
        return IndexColors.Brak.value
    }
}