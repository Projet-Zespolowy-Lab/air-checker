package com.example.air_checker.database

data class Measure(
  val id: Int,
  val krajowyIndeks: Double,
  val kolor: String,
  val timestamp: String
)

data class MeasureHistory(
  val history: List<Measure>
)