package com.example.air_checker.model

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.air_checker.view.MainActivity
import com.example.air_checker.view.WelcomeActivity


@SuppressLint("ComposableNaming")
@Composable
fun checkFirstBoot() {
    val context = LocalContext.current
    val startDestination: String
    val firstBoot = context.getSharedPreferences("BOOT_PREF", MODE_PRIVATE).getBoolean("firstBoot", true)
    Log.d("fb", firstBoot.toString()
    )
    if (firstBoot) {
        startDestination = "start"
        context.getSharedPreferences("BOOT_PREF", MODE_PRIVATE)
            .edit()
            .putBoolean("firstBoot", false)
            .apply()
    }
    else{
        startDestination="home"
    }
    NavigateTo(startDestination)
}

@Composable
fun NavigateTo(startDestination: String) {
    val context = LocalContext.current
    when(startDestination){
        "start" -> { context.startActivity(Intent(context, WelcomeActivity::class.java)) }
        "home" -> { context.startActivity(Intent(context, MainActivity::class.java)) }
    }
}