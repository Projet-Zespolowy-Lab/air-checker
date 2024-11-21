package com.example.air_checker.model

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.air_checker.view.MainActivity
import com.example.air_checker.view.WelcomeView


class AppNavigator {


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
        navigate(startDestination)
    }

    @SuppressLint("ComposableNaming")
    @Composable
    fun navigate(startDestination: String) {
        val context = LocalContext.current
        val navigator: NavHostController = rememberNavController()
        NavHost(navController = navigator, startDestination = startDestination) {
            composable("start") { WelcomeView(navigator, "Zaczynajmy") }
            composable("home") { context.startActivity(Intent(context, MainActivity::class.java)) }

        }
    }


}