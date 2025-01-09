package com.example.air_checker.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.core.app.ActivityCompat
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.air_checker.R
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.IndexColors
import com.example.air_checker.model.Station
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

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
    val result = ActivityCompat.checkSelfPermission(context,permission)

    return result == PackageManager.PERMISSION_GRANTED
}

fun createLocationRequest(): LocationRequest {
    return LocationRequest.Builder(1000).build()
}

private lateinit var locationClient: FusedLocationProviderClient

@SuppressLint("MissingPermission")
fun initUpdates(viewModel: LocationViewModel, activity: Activity) {
    locationClient = LocationServices.getFusedLocationProviderClient(activity)

    locationClient.requestLocationUpdates(
        createLocationRequest(),
        {location -> viewModel.update(location.latitude, location.longitude)},
        Looper.getMainLooper()
    )
}

fun checkStoragePermission(context: Context){
    val activity = context as Activity
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }

}

private const val DayTime = "06:00"
private const val NightTime = "18:00"

@Composable
fun getImageBitmap(): ImageBitmap {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalDateTime.now().format(formatter)
    if(LocalTime.parse(time, formatter) > LocalTime.parse(NightTime, formatter) || LocalTime.parse(time, formatter) < LocalTime.parse(DayTime, formatter))
        return ImageBitmap.imageResource(id =R.drawable.background_night)
    return ImageBitmap.imageResource(id =R.drawable.background_day)
}

fun checkIfIsNight(): Boolean {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalDateTime.now().format(formatter)
    return (LocalTime.parse(time, formatter) > LocalTime.parse(NightTime, formatter) || LocalTime.parse(time, formatter) < LocalTime.parse(DayTime, formatter))
}

// Funkcja nasłuchująca stanu połączenia sieciowego
fun observeNetworkConnectivity(context: Context, stationsViewModel: StationsViewModel) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            stationsViewModel.setNetworkError(false)
        }

        override fun onLost(network: Network) {
            stationsViewModel.setNetworkError(true)
        }
    })
}


// Funkcja sprawdzająca dostępność połączenia sieciowego (na żądanie)
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels;
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels;
}
