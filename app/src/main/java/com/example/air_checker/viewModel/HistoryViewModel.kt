package com.example.air_checker.viewModel

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.example.air_checker.database.Measure
import java.io.File
import java.io.OutputStreamWriter


fun loadMoreItems(startIndex: Int, allItems: List<Measure>, displayedItems: MutableList<Measure>) {
    val endIndex = minOf(startIndex + 6, allItems.size)
    displayedItems.addAll(allItems.subList(startIndex, endIndex))
}


fun saveCsvToDocuments(context: Context, fileName: String, measures: List<Measure>) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/")
    }
    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use {
            outputStream -> OutputStreamWriter(outputStream, Charsets.UTF_16).use {
                writer -> writer.write(""""Data"; "Miejsce stacji pomiarowej"; "Jakość Powietrza"; "Jakość PM 2.5"; "Jakość PM10"; "Jakość NO₂"; "Jakość SO₂"; "Jakość O₃"""")
                writer.write("\n")
            measures.forEach {
                measure -> writer.write("${measure.timestamp}; ${measure.place}; ${measure.qualityCategory}; ${measure.pm25}; ${measure.pm10}; ${measure.no2}; ${measure.so2}; ${measure.o3}")
                writer.write("\n") }
            }
        }
    }
}

fun fileExists(context: Context, fileName: String): Boolean {
    val file = File(context.filesDir, fileName)
    return file.exists()
}