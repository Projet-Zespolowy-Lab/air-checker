package com.example.air_checker.viewModel

import com.example.air_checker.database.Measure
import com.example.air_checker.model.Place


fun loadMoreItems(startIndex: Int, allItems: List<Measure>, displayedItems: MutableList<Measure>) {
    val endIndex = minOf(startIndex + 20, allItems.size)
    displayedItems.addAll(allItems.subList(startIndex, endIndex))
}