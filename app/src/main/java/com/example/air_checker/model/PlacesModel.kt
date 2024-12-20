package com.example.air_checker.model

import fetchAllPlaces

data class Place(
  val name: String,
  val county: String,
  val voivodeship: String,
  val lat: Double,
  val lon: Double
)
lateinit var places: List<Place>
