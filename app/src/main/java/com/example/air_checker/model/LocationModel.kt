package com.example.air_checker.model

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LocationModel {
    private val client = OkHttpClient()

    fun getGeolocation(apiKey: String): Pair<Double, Double>? {
        val url = "https://www.googleapis.com/geolocation/v1/geolocate?key=$apiKey"
        val jsonBody = "{}"
        val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        return try {
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            Log.d("getGeolocation - request", request.toString())
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("getGeolocation", response.toString())
                    return null
                }

                val responseBody = response.body?.string() ?: return null
                val jsonResponse = JSONObject(responseBody)
                val location = jsonResponse.getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")

                Pair(lat, lng)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
