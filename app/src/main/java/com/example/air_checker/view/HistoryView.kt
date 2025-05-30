package com.example.air_checker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.air_checker.R
import com.example.air_checker.database.Measure
import com.example.air_checker.viewModel.fileExists
import com.example.air_checker.viewModel.getColor
import com.example.air_checker.viewModel.loadMoreItems
import com.example.air_checker.viewModel.saveCsvToDocuments
import deleteFromDatabase
import kotlinx.coroutines.launch
import readRecordsFromDatabase
import java.time.LocalDateTime

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val items = readRecordsFromDatabase(this).history
            HistoryView(items)
        }
    }
}

@Composable
fun HistoryView(items: List<Measure>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState()}
    val selectedIndex = remember { mutableIntStateOf(3) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val displayedItems = remember { mutableStateListOf<Measure>() }
    val listState = rememberLazyListState()
    val listSize by rememberUpdatedState(displayedItems.size)
    var showBottomGradient by remember { mutableStateOf(true) }
    var showTopGradient by remember { mutableStateOf(true) }
    val firstVisibleItemIndex = remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: -1 } }
    val lastVisibleItemIndex = remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 } }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 15.dp, top = 15.dp, bottom = 10.dp), // Padding top, aby ustawić je odpowiednio od góry
            horizontalArrangement = Arrangement.End // Umieszcza przycisk po prawej stronie
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF80E4FF), CircleShape)
                    .size(50.dp),
                contentAlignment = Alignment.Center// Wielkość koła
            ) {
                Image(
                    painter = painterResource(R.drawable.menu_black),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(top = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Lista z maską
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 550.dp)
        ) {
            LaunchedEffect(Unit) {
                loadMoreItems(currentIndex, items, displayedItems)
                currentIndex += 6
            }
            LaunchedEffect(lastVisibleItemIndex.value) {
                showBottomGradient = lastVisibleItemIndex.value != listSize - 1 && listSize > lastVisibleItemIndex.value
            }
            LaunchedEffect(firstVisibleItemIndex.value) {
                showTopGradient = firstVisibleItemIndex.value != 0
            }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
            ) {

                itemsIndexed(displayedItems){ index,item ->
                    ColoredBox(item.qualityCategory!!, item.timestamp!!, item.place!!, item.pm25!!, item.pm10!!, item.no2!!, item.so2!!, item.id!!, displayedItems)
                    if (index >= displayedItems.size - 1 && currentIndex < items.size) {
                        LaunchedEffect(Unit) {
                            loadMoreItems(currentIndex, items, displayedItems)
                            currentIndex += 6
                        }
                    }
                }
            }
            if (showBottomGradient)
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xFFF5FBFF)),
                                startY = 0f,
                                endY = 100f
                            )
                        )
                        .align(Alignment.BottomCenter)
                )
            }
            if (showTopGradient)
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFF5FBFF), Color.Transparent),
                                startY = 0f,
                                endY = 100f
                            )
                        )
                        .align(Alignment.TopCenter)
                )
            }
        }
        TextButton(
            onClick = {
                try {
                    val fileName = "wyniki_pomiarów.csv"
                    saveCsvToDocuments(context, fileName, readRecordsFromDatabase(context).history)
                    scope.launch {
                        snackBarHostState.showSnackbar("Plik z wynikami został utworzony w folderze Documents.")
                    }
                }
                catch(e: Exception){
                    scope.launch {
                        snackBarHostState.showSnackbar("Nie udało się zapisać pliku.")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp)
                .background(Color(0xFF80E4FF), shape = RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.export),
                contentDescription = null,
                modifier = Modifier.size(24.dp)

            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Eksportuj",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.prompt)),
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
        SnackbarHost(hostState = snackBarHostState)
        NavMenu(selectedIndex.intValue)
    }
}

@Composable
fun ColoredBox(
    colorFlag: String,
    date: String,
    place: String,
    valuePM25: String,
    valuePM10: String,
    valueNO2: String,
    valueSO2: String,
    itemID: Int,
    displayedItems: SnapshotStateList<Measure>
) {
    val flagColor = Color(getColor(colorFlag))
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFF80E4FF), shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = date,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp, top = 4.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = place,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp, top = 4.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Button(
                    onClick = {
                        deleteFromDatabase(context, id = itemID)
                        displayedItems.removeIf { it.id == itemID }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(26.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(R.drawable.button_close),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "PM 2.5 [$valuePM25] PM 10 [$valuePM10]\nNO 2 [$valueNO2] SO 2 [$valueSO2]",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F),
                    maxLines = 2, // Pozwolenie na maksymalnie dwa wiersze
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(
                        flagColor,
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}



@Preview(name = "NEXUS_5", device = Devices.NEXUS_5, apiLevel = 33, showBackground = true)
@Preview(name = "NEXUS_6", device = Devices.NEXUS_6, apiLevel = 33, showBackground = true)
@Preview(name = "NEXUS_5X", device = Devices.NEXUS_5X, apiLevel = 33, showBackground = true)
@Preview(name = "NEXUS_6P", device = Devices.NEXUS_6P, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL", device = Devices.PIXEL, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_XL", device = Devices.PIXEL_XL, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_2", device = Devices.PIXEL_2, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_2_XL", device = Devices.PIXEL_2_XL, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_3", device = Devices.PIXEL_3, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_3A", device = Devices.PIXEL_3A, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_3A_XL", device = Devices.PIXEL_3A_XL, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_4", device = Devices.PIXEL_4, apiLevel = 33, showBackground = true)
@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, apiLevel = 33, showBackground = true)
@Composable
fun HistoryPreview() {
    val measure = mutableListOf<Measure>()
    for (i in 1..10)
    {
        measure.add(Measure(i, "Warszawa", 0.0, "Nieznany", null, "Nieznany", "Nieznany","Nieznany", "Nieznany", "Nieznany",
            LocalDateTime.now().toLocalDate().toString()))
    }
    HistoryView(measure)
}