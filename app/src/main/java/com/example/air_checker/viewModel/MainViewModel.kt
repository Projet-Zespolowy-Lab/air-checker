package com.example.air_checker.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.IndexColors
import com.example.air_checker.model.Station
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


fun getNameNearestStation(nearestStation: Station?): String {
    nearestStation.let { station ->
        if (station != null) {
            return station.name
        }
    }
    return ""
}

fun getQuality(airQualityCategories: AirQualityCategories?, indexName: String): String {
    airQualityCategories.let { categories ->
        if (categories != null) {
            val categoryList = categories.airQualityCategories
            for (category in categoryList) {
                if (category.categoryName == indexName) {
                    if (category.qualityValue != null) {
                        return category.qualityValue
                    }
                    return "Nieznany"
                }
            }
        }
    }
    return "Nieznany"
}
fun getColor(indexValue: String): Long {
    when(indexValue){
        "Bardzo dobry" -> return IndexColors.Bardzo_Dobry.rgb
        "Dobry" -> return IndexColors.Dobry.rgb
        "Umiarkowany" -> return IndexColors.Umiarkowany.rgb
        "Dostateczny" -> return IndexColors.Dostateczny.rgb
        "Zły" -> return IndexColors.Zły.rgb
        "Bardzo zły" -> return IndexColors.Bardzo_Zły.rgb
    }
    return IndexColors.Brak.rgb
}

fun getPercentageAirPurity(indexValue: String): String {
    when(indexValue){
        "Bardzo dobry" -> return IndexColors.Bardzo_Dobry.value
        "Dobry" -> return IndexColors.Dobry.value
        "Umiarkowany" -> return IndexColors.Umiarkowany.value
        "Dostateczny" -> return IndexColors.Dostateczny.value
        "Zły" -> return IndexColors.Zły.value
        "Bardzo zły" -> return IndexColors.Bardzo_Zły.value
    }
    return IndexColors.Brak.value
}

private val LOCATION_PERMISSION_REQUEST_CODE = 1

fun checkPermissions(context: Context) {
    val activity = context as Activity
    if (!hasLocationPermissions(context)) {
        requestLocationPermissions(activity)
        return
    }
}

fun requestLocationPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        LOCATION_PERMISSION_REQUEST_CODE
    )
}

fun hasLocationPermissions(context: Context): Boolean {
    return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, context) &&
            hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)
}

fun hasPermission(permission: String, context: Context): Boolean {
    val result = ActivityCompat.checkSelfPermission(context,permission);

    return result == PackageManager.PERMISSION_GRANTED;
}

fun createLocationRequest(): LocationRequest {
    return LocationRequest.Builder(1000).build()
}

private lateinit var locationClient: FusedLocationProviderClient

@SuppressLint("MissingPermission")
fun initUpdates(viewModel: LocationViewModel, activity: Activity) {
    locationClient = LocationServices.getFusedLocationProviderClient(activity);

    locationClient.requestLocationUpdates(
        createLocationRequest(),
        {location -> viewModel.update(location.latitude, location.longitude)},
        Looper.getMainLooper()
    )
}

fun checkStoragePermission(context: Context){
    val activity = context as Activity
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

}
