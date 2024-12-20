package com.example.air_checker.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.air_checker.R
import com.example.air_checker.database.Measure
import com.example.air_checker.database.MeasureHistory
import com.example.air_checker.model.AirQualityCategories
import com.example.air_checker.model.Place
import com.example.air_checker.model.Station
import com.example.air_checker.viewModel.AirQualityIndexViewModel
import com.example.air_checker.viewModel.LocationViewModel
import com.example.air_checker.viewModel.StationsViewModel
import com.example.air_checker.viewModel.checkPermissions
import com.example.air_checker.viewModel.getColor
import com.example.air_checker.viewModel.getNameNearestStation
import com.example.air_checker.viewModel.getPercentageAirPurity
import com.example.air_checker.viewModel.getQuality
import com.example.air_checker.viewModel.initUpdates
import deleteFromDatabase
import insertRecordToDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import readRecordsFromDatabase


class MainActivity : ComponentActivity() {
    private val stationsViewModel: StationsViewModel by viewModels()
    private val airQualityIndexViewModel: AirQualityIndexViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkPermissions(this)
        val viewModel = LocationViewModel()

        // Obserwacja połączenia sieciowego
        observeNetworkConnectivity()

        if(intent.getStringExtra("wybrane_miasto_nazwa") == null){
            // Regularne pobieranie lokalizacji i stacji co sekunde
            lifecycleScope.launch {
                while (true) {
                    if (isNetworkAvailable()) {
                        initUpdates(viewModel, this@MainActivity)
                        stationsViewModel.setNetworkError(false) // reset błędu sieci
                    } else {
                        stationsViewModel.setNetworkError(true) // ustawienie błędu sieci
                        Log.d("MainActivity", "Brak połączenia z internetem")
                    }

                    delay(1000) // odświeżanie co sekunde
                }
            }

            lifecycleScope.launch {
                viewModel.state.collectLatest { coordinates ->
                    if (isNetworkAvailable() && coordinates.latitude != 0.0 && coordinates.longitude != 0.0) {
                        stationsViewModel.fetchStations(coordinates.latitude, coordinates.longitude)
                        stationsViewModel.setNetworkError(false) // reset błędu sieci po udanym pobraniu
                    } else {
                        stationsViewModel.setNetworkError(true)
                        Log.d("MainActivity", "Brak połączenia lub brak współrzędnych")
                    }
                }
            }
        }
        else
        {
            // Regularne pobieranie lokalizacji i stacji co sekunde
            lifecycleScope.launch {
                while (true) {
                    if (isNetworkAvailable()) {
                        stationsViewModel.setNetworkError(false) // reset błędu sieci
                    } else {
                        stationsViewModel.setNetworkError(true) // ustawienie błędu sieci
                        Log.d("MainActivity", "Brak połączenia z internetem")
                    }

                    delay(1000) // odświeżanie co sekunde
                }
            }
            lifecycleScope.launch {
                if (isNetworkAvailable()) {
                    stationsViewModel.fetchStations(
                        intent.getDoubleExtra("wybrane_miasto_lat", 0.0), intent.getDoubleExtra("wybrane_miasto_lon", 0.0)
                    )
                    stationsViewModel.setNetworkError(false) // reset błędu sieci po udanym pobraniu
                } else {
                    stationsViewModel.setNetworkError(true)
                    Log.d("MainActivity", "Brak połączenia lub brak współrzędnych")
                }
            }
        }

        /****Usunąć po implementacji odczytu i zapisu do bazy*********/
        // Dodanie nowego rekordu
        val measure = Measure(
            place = "Jana Pawła II, Łódź",
            qualityIndex = 78.3,
            qualityCategory = "Dobry",
            color = "#0011AA",
            pm10 = "Nieznany",
            pm25 = "Dobry",
            no2 = "Dobry",
            so2 = "Zły",
            o3 = "Nieznany")
        insertRecordToDatabase(this, measure)

        // Odczyt rekordów z bazy danych
        val measureHistory: MeasureHistory = readRecordsFromDatabase(this)

        //Usuwanie z bazy po ID
        deleteFromDatabase(this, id=5)

        // Logujemy każdy odczytany rekord
        measureHistory.history.forEach { measure ->
            Log.d("baza",
                 "ID: ${measure.id}, " +
                "Place: ${measure.place}, " +
                "Index: ${measure.qualityIndex}, " +
                "Cat: ${measure.qualityCategory}, " +
                "Kolor: ${measure.color}, " +
                "PM10: ${measure.pm10}, " +
                "PM2.5: ${measure.pm25}, " +
                "NO2: ${measure.no2}, " +
                "SO2: ${measure.so2}, " +
                "O3: ${measure.o3}, " +
                "Timestamp: ${measure.timestamp}")
        }

        /***********Koniec usunąć************************************/


        setContent {
            NearestStation(stationsViewModel = stationsViewModel,
                airQualityIndexViewModel = airQualityIndexViewModel, ::isNetworkAvailable)
        }
    }


    // Funkcja nasłuchująca stanu połączenia sieciowego
    private fun observeNetworkConnectivity() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}



@Composable
fun NearestStation(stationsViewModel: StationsViewModel,
                       airQualityIndexViewModel: AirQualityIndexViewModel, isNetworkAvailable: ()->Boolean)
{
    val nearestStation by stationsViewModel.nearestStation.observeAsState()
    val networkError by stationsViewModel.networkError.observeAsState(false)

    Column() {
        when {
            networkError -> {
                // Komunikat o braku internetu
                Text(text = "Brak połączenia z internetem. Nie można pobrać stacji.")
            }
        }
        val airQualityCategories by airQualityIndexViewModel.airQualityCategories.observeAsState()
        nearestStation?.let { airQualityIndexViewModel.fetchSensorsDataByStationId(it.id) }
        if(isNetworkAvailable()){
            MainView(nearestStation, airQualityCategories)
        }
        else{
            MainView(Station(0, "",0.0,0.0),)
        }
    }
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
fun MainView(
    nearestStation: Station? = Station(999, "Warsaw", 0.0, 0.0, 0.0),
    airQuality: AirQualityCategories? = AirQualityCategories(listOf())
) {
    val context = LocalContext.current
    val selectedIndex = remember { mutableStateOf(0) } // Zapamiętuje wybraną zakładkę

    Column(Modifier.fillMaxSize().statusBarsPadding().systemBarsPadding()) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 15.dp)
        ) {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = CircleShape,
                modifier = Modifier.size(31.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    val intent = Intent(context, WelcomeActivity::class.java)
                    intent.putExtra("buttonText", "Powrót")
                    context.startActivity(intent)
                },
            ) {
                Image(
                    painter = painterResource(R.drawable.info),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(240.dp)
                .drawBehind {
                    drawCircle(
                        color = Color(getColor(getQuality(airQuality, "Krajowy indeks jakości powietrza"))),
                        radius = 320f
                    )
                    drawCircle(
                        color = Color(0xFFFFE9C9),
                        radius = 305f
                    )
                }
        ) {
            Column(modifier = Modifier.align(Alignment.TopCenter)) {
                Text(
                    text = "AIR METER",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                    modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = 30.dp).padding(top = 15.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = getPercentageAirPurity(getQuality(airQuality, "Krajowy indeks jakości powietrza")),
                    fontSize = 75.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = getQuality(airQuality, "Krajowy indeks jakości powietrza"),
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
                text = getNameNearestStation(nearestStation) + ", PL",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            )
        }

        Spacer(Modifier.height(50.dp))

        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            IndexField("PM 2.5", getQuality(airQuality, "PM2.5"))
            IndexField("PM 10", getQuality(airQuality, "PM10"))
            IndexField("NO₂", getQuality(airQuality, "NO2"))
            IndexField("SO₂", getQuality(airQuality, "SO2"))
            IndexField("O₃", getQuality(airQuality, "O3"))
        }

        Spacer(Modifier.weight(1f)) // Dodanie odstępu między głównym widokiem a nawigacją

        NavMenu(selectedIndex.value) { index ->
            selectedIndex.value = index
        }
    }
}

@Composable
fun NavMenu(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // Przestrzeń nad paskiem menu

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f) // Pasek zajmuje 80% szerokości ekranu
                .height(screenHeight * 0.075f)
                .clip(RoundedCornerShape(50.dp)) // Zaokrąglenie rogów
                .background(Color(0xFF80E4FF)) // Kolor tła paska
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Równomierne rozmieszczenie elementów
                verticalAlignment = Alignment.CenterVertically // Wycentrowanie wertykalne
            ) {
                // Przycisk Home
                NavMenuItem(
                    isSelected = selectedIndex == 0,
                    icon = R.drawable.ic_home,
                    onClick = {
                        if (selectedIndex != 0) {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                )

                // Przycisk Search
                NavMenuItem(
                    isSelected = selectedIndex == 1,
                    icon = R.drawable.ic_search,
                    onClick = {
                        val intent = Intent(context, ScreenActivity::class.java)
                        context.startActivity(intent)
                    }
                )

                // Przycisk Info
                NavMenuItem(
                    isSelected = selectedIndex == 2,
                    icon = R.drawable.info,
                    onClick = { onItemSelected(2) }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp)) // Przestrzeń pod paskiem menu
    }
}

@Composable
fun NavMenuItem(isSelected: Boolean, icon: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center, // Wycentrowanie zawartości w Box
        modifier = Modifier
            .size(50.dp) // Rozmiar okrągłego przycisku
            .clip(CircleShape) // Zaokrąglenie krawędzi
            .background(if (isSelected) Color.White else Color.Transparent) // Tło zaznaczonego elementu
            .clickable { onClick() } // Obsługa kliknięcia
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.6f), // Kolor ikony
            modifier = Modifier.size(30.dp) // Rozmiar ikony
        )
    }
}
