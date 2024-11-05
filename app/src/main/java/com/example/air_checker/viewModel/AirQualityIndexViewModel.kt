package com.example.air_checker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.air_checker.model.AirQualityIndex
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class AirQualityIndexViewModel : ViewModel() {
  private val client = OkHttpClient()
  private val _sensorData = MutableLiveData<String>()
  val sensorData: LiveData<String> = _sensorData

  fun fetchSensorsDataByStationId(stationId: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      val url = HttpUrl.Builder()
        .scheme("https")
        .host("api.gios.gov.pl")
        .addPathSegment("pjp-api")
        .addPathSegment("v1")
        .addPathSegment("rest")
        .addPathSegment("aqindex")
        .addPathSegment("getIndex")
        .addPathSegment(stationId.toString())
        .build()

      val request = Request.Builder().url(url).build()
      Log.d("fetchSensorsDataByStationId", request.toString())

      try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        Log.d("fetchSensorsDataByStationId - reponseBody", responseBody.toString())

        responseBody?.let {
          Log.d("fetchSensorsDataByStationId - it", it)
          val airQualityIndex = parseAirQualityIndexResponse(it)
          Log.d("airQualityIndex", airQualityIndex.toString())
          _sensorData.postValue(airQualityIndex.toString())
        } ?: _sensorData.postValue("Brak danych")
      } catch (e: Exception) {
        _sensorData.postValue("Błąd: ${e.message}")
      }
    }
  }

  // Parsuje odpowiedź z API do AirQualityIndex
  fun parseAirQualityIndexResponse(json: String): AirQualityIndex? {
    val gson = Gson()

    // Parsujemy główny obiekt JSON, aby wyciągnąć część "AqIndex"
    val jsonObject = JsonParser.parseString(json).asJsonObject
    val aqIndexObject = jsonObject.getAsJsonObject("AqIndex") ?: return null

    Log.d("parseAirQualityIndexResponse", aqIndexObject.toString())
    // Tworzymy obiekt AirQualityIndex z JSON
    return gson.fromJson(aqIndexObject, AirQualityIndex::class.java)
  }
}

