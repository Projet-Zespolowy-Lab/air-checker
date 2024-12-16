package com.example.air_checker.database

import com.example.air_checker.model.AirQualityIndex

data class Measure(
  val id: Int? = null,
  val nationalAirQualityIndex: Double? = null,
  val color: String? = null,
  val pm10: String? = null,
  val pm25: String? = null,
  val no2: String? = null,
  val so2: String? = null,
  val o3: String? = null,
  val timestamp: String? = null
)

data class MeasureHistory(
  val history: List<Measure>
)