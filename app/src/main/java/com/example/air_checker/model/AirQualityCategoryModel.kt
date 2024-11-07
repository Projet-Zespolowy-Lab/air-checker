package com.example.air_checker.model


data class AirQualityCategory(
  val categoryName: String,
  val qualityValue: String?
)

data class AirQualityCategories(
  val airQualityCategories: List<AirQualityCategory>
)


