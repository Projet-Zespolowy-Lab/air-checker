package com.example.air_checker.viewModel;
import com.example.air_checker.model.Place

fun filterPlacesByFirstLetter(places: List<Place>, query: String): List<Place> {
    return places
        .filter { it.name.startsWith(query, ignoreCase = true) }
        .sortedBy { it.name }
}