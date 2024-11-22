package com.example.air_checker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.air_checker.model.checkFirstBoot

class FirstStart: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            checkFirstBoot()
        }
    }
}