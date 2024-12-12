package com.example.air_checker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.air_checker.R

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestView()
        }
    }
}
@Composable
fun TestView() {
    val selectedIndex = remember { mutableStateOf(0) } // Zapamiętuje wybraną zakładkę

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedIndex.value) { index ->
                selectedIndex.value = index
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            when (selectedIndex.value) {
                0 -> HomeContent()
                1 -> SearchContent()
                2 -> ProfileContent()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color(0xFF80E4FF),
        contentColor = Color.White,
        modifier = Modifier.height(82.dp) // Zmniejszona wysokość paska nawigacyjnego
    ) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Home",
                    modifier = Modifier.size(20.dp) // Mniejsza ikona
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 10.sp // Mniejszy rozmiar tekstu
                )
            }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp) // Mniejsza ikona
                )
            },
            label = {
                Text(
                    text = "Search",
                    fontSize = 10.sp // Mniejszy rozmiar tekstu
                )
            }
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(20.dp) // Mniejsza ikona
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 10.sp // Mniejszy rozmiar tekstu
                )
            }
        )
    }
}

@Composable
fun HomeContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Home Screen",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Search Screen",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Profile Screen",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}

