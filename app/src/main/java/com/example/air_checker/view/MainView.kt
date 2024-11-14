package com.example.air_checker.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.air_checker.BuildConfig
import com.example.air_checker.R
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.Station
import com.example.air_checker.viewModel.AirQualityIndexViewModel
import com.example.air_checker.viewModel.LocationViewModel
import com.example.air_checker.viewModel.MainViewModel
import com.example.air_checker.viewModel.StationsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val airQualityIndexViewModel: AirQualityIndexViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiKey = BuildConfig.API_KEY
        locationViewModel.fetchLocation(apiKey)

        // Wywołanie fetchStations z podanymi współrzędnymi
        lifecycleScope.launch {
            locationViewModel.coordinates.collectLatest { coordinates ->
                if (coordinates != null) {
                    // Przekaż współrzędne do stationsViewModel
                    stationsViewModel.fetchStations(coordinates.latitude, coordinates.longitude)


                } else {
                    // Obsłuż błąd pobierania współrzędnych
                    Log.d("Error while fetching locations", "coordinates are null")
                }
            }
        }

        setContent {
            NearestStation(stationsViewModel = stationsViewModel,
                               airQualityIndexViewModel = airQualityIndexViewModel)
        }
    }
}

@Composable
fun NearestStation(stationsViewModel: StationsViewModel,
                       airQualityIndexViewModel: AirQualityIndexViewModel)
{
    val nearestStation by stationsViewModel.nearestStation.observeAsState()
    val airQualityCategories by airQualityIndexViewModel.airQualityCategories.observeAsState()
    nearestStation?.let { airQualityIndexViewModel.fetchSensorsDataByStationId(it.id) }
    MainView(nearestStation, airQualityCategories)
}

@Composable
fun IndexField(indexName: String, indexValue: String){
    Row(modifier = Modifier.fillMaxWidth().height(30.dp)) {
        Spacer(Modifier.width(10.dp))
        Image(
            painter = painterResource(R.drawable.info_blue),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = indexName,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal))
        )
        Spacer(Modifier.weight(0.8f))
        Text(
            text = indexValue,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            modifier = Modifier.align(Alignment.CenterVertically).offset(x = (-10).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainView(nearestStation: Station? = Station(999, "Warsaw",0.0,0.0,0.0), airQuality: AirQualityCategories? = AirQualityCategories(listOf())) {
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp)
        ){
            Image(
                painter = painterResource(R.drawable.info),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(31.dp, 31.dp)
            )
        }
        Spacer(Modifier.height(30.dp))
        Box(modifier = Modifier.fillMaxWidth().size(240.dp).drawBehind {
            drawCircle(
                color = Color(MainViewModel().getColor(MainViewModel().getQuality(airQuality, "Krajowy indeks jakości powietrza"))),
                radius = 320f
            )
            drawCircle(
                color = Color(0xFFFFE9C9),
                radius = 305f
            )
        }) {
            Column(modifier = Modifier.align(Alignment.TopCenter)){
                Text(
                    text = "AIR METER",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                    modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = 30.dp).padding(top = 15.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = MainViewModel().getPercentageAirPurity(MainViewModel().getQuality(airQuality, "Krajowy indeks jakości powietrza")),
                    fontSize = 75.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = MainViewModel().getQuality(airQuality, "Krajowy indeks jakości powietrza"),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                    modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = (-30).dp)
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(15.dp, 15.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = MainViewModel().getNameNearestStation(nearestStation) + ", PL",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            )
        }
        Spacer(Modifier.height(50.dp))
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            IndexField("PM 2.5", MainViewModel().getQuality(airQuality, "PM2.5"))
            IndexField("PM 10", MainViewModel().getQuality(airQuality, "PM10"))
            IndexField("NO 2", MainViewModel().getQuality(airQuality, "NO2"))
            IndexField("SO 2", MainViewModel().getQuality(airQuality, "SO2"))
            IndexField("O 3", MainViewModel().getQuality(airQuality, "O3"))

        }
    }
}
