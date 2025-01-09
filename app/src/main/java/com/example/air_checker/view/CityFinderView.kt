package com.example.air_checker.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.air_checker.R
import com.example.air_checker.model.Place
import com.example.air_checker.model.places
import com.example.air_checker.viewModel.filterPlacesByFirstLetter
import com.example.air_checker.viewModel.loadMoreItems

class CityFinderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityFinderView()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CityFinderView() {

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .statusBarsPadding()
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        var items = filterPlacesByFirstLetter(places, "")
        val selectedIndex = remember { mutableIntStateOf(1) }
        val displayedItems = remember { mutableStateListOf<Place>() }
        var currentIndex by remember { mutableIntStateOf(0) }
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        var text by remember { mutableStateOf(TextFieldValue("")) }
        val filteredItems = if (text.text.isEmpty()) displayedItems else filterPlacesByFirstLetter(places, text.text)
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            loadMoreItems(currentIndex, items, displayedItems)
            currentIndex += 20
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                displayedItems.clear()
                currentIndex = 0
                loadMoreItems(currentIndex, items, displayedItems)
                currentIndex +=20
            },
            label = { Text("Wyszukaj miasto...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )
        Spacer(Modifier.height(screenHeight * 0.005f))
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.75f)
        ){
            itemsIndexed(filteredItems){ index, item ->
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("wybrane_miasto_nazwa", item.name)
                        intent.putExtra("wybrane_miasto_lat", item.lat)
                        intent.putExtra("wybrane_miasto_lon", item.lon)
                        context.startActivity(intent)
                    },
                    shape = RectangleShape,
                    border = null,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_blue),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp, 22.dp).padding(end =  10.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        text = item.name + ", woj. " + item.voivodeship
                    )
                    if (index >= filteredItems.size - 1 && currentIndex < items.size) {
                        LaunchedEffect(Unit) {
                            loadMoreItems(currentIndex, items, displayedItems)
                            currentIndex += 20
                        }
                    }
                }
            }
        }
        NavMenu(selectedIndex.intValue)
    }
}