package com.example.air_checker.model

import android.location.Location
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

// Klasa reprezentująca stację
data class Station(
  val id: Int,
  val lat: Float,
  val lon: Float,
  var distanceTo: Float = Float.MAX_VALUE // Odległość do punktu referencyjnego
)

// Klasa odpowiadająca strukturze danych z odpowiedzi API
data class StationResponse(
  val id: Int,
  val stationName: String,
  val gegrLat: String,
  val gegrLon: String
)

// Klasa odpowiedzialna za zarządzanie stacjami i obliczanie odległości
class Stations(
  private val lat: Float,  // Lokalizacja użytkownika (punkt referencyjny)
  private val lon: Float,
  val listStations: List<Station>
) {
  // Inicjalizator, który automatycznie oblicza odległości
  init {
    calculateDistances()
    nearestStation()
  }

  // Funkcja obliczająca odległości każdej stacji od punktu referencyjnego
  private fun calculateDistances() {
    listStations.forEach { station ->
      station.distanceTo = calculateDistance(
        lat.toDouble(),
        lon.toDouble(),
        station.lat.toDouble(),
        station.lon.toDouble()
      )
    }
  }

  // Funkcja zwracająca najbliższą stację
  fun nearestStation(): Station? {
    return listStations.minByOrNull { it.distanceTo }
  }

  // Funkcja obliczająca odległość między dwoma punktami geograficznymi
  private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val startPoint = Location("startPoint").apply {
      latitude = lat1
      longitude = lon1
    }

    val endPoint = Location("endPoint").apply {
      latitude = lat2
      longitude = lon2
    }

    return startPoint.distanceTo(endPoint)  // Odległość w metrach
  }
}

// Funkcja pobierająca stacje z API
fun getAllStations(onResult: (Stations) -> Unit, onError: (String) -> Unit, userLat: Float, userLon: Float) {
  val client = OkHttpClient()

  val url = HttpUrl.Builder()
    .scheme("https")
    .host("api.gios.gov.pl")
    .addPathSegment("pjp-api")
    .addPathSegment("rest")
    .addPathSegment("station")
    .addPathSegment("findAll")
    .build()

  val request = Request.Builder()
    .url(url)
    .build()

  CoroutineScope(Dispatchers.IO).launch {
    try {
      val response = client.newCall(request).execute()
      val responseBody = response.body?.string()

      responseBody?.let {
        val stationsList = parseStationsJson(it)
        val stations = Stations(userLat, userLon, stationsList)
        withContext(Dispatchers.Main) {
          onResult(stations)
        }

      } ?: withContext(Dispatchers.Main) {
        onError("Brak odpowiedzi")
      }
    } catch (e: Exception) {
      withContext(Dispatchers.Main) {
        onError("Błąd: ${e.message}")
      }
    }
  }
}

// Funkcja parsująca odpowiedź z API do listy stacji
fun parseStationsJson(json: String): List<Station> {
  val gson = Gson()
  val stationListType = object : TypeToken<List<StationResponse>>() {}.type
  val stationResponseList: List<StationResponse> = gson.fromJson(json, stationListType)

  return stationResponseList.map {
    Station(
      it.id,
      it.gegrLat.toFloatOrNull() ?: 0.0f,
      it.gegrLon.toFloatOrNull() ?: 0.0f
    )
  }
}
