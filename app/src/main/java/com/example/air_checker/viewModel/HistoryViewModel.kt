package com.example.air_checker.viewModel

import com.example.air_checker.database.Measure
import java.io.OutputStream


fun loadMoreItems(startIndex: Int, allItems: List<Measure>, displayedItems: MutableList<Measure>) {
    val endIndex = minOf(startIndex + 6, allItems.size)
    displayedItems.addAll(allItems.subList(startIndex, endIndex))
}

fun OutputStream.exportToCSV(measures: List<Measure>){
    val writer = bufferedWriter()
    writer.write(""""Data"; "Miejsce stacji pomiarowej"; "Jakość Powietrza"; "Jakość PM 2.5"; "Jakość PM10"; "Jakość NO₂"; "Jakość SO₂"; "Jakość O₃"""")
    writer.newLine()
    measures.forEach {
        writer.write("${it.timestamp}; ${it.place}; ${it.qualityCategory}; ${it.pm25}; ${it.pm10}; ${it.no2}; ${it.so2}; ${it.o3}")
        writer.newLine()
    }
    writer.flush()
}