package com.example.air_checker.database

data class Measure(
  val id: Int? = null,
  val krajowyIndeks: Double,
  val kolor: String,
  val timestamp: String? = null
)

data class MeasureHistory(
  val history: List<Measure>
)