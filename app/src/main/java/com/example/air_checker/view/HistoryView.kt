package com.example.air_checker.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.air_checker.R
import com.example.air_checker.model.IndexColors

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HistoryView()
        }
    }
}

@Composable
fun HistoryView() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val context = LocalContext.current
    val selectedIndex = remember { mutableStateOf(3) }
    Column(
        Modifier
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

        Spacer(modifier = Modifier.height(20.dp))

        // Lista z maską
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 550.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
            ) {
                ColoredBox(colorFlag = "Bardzo_Dobry", "17-12-2024 20:23:23", "Jana Pawła II, Łódź", "Bardzo dobry", "Dobry", "Dobry", "Bardzo dobry")
                ColoredBox(colorFlag = "Dobry", "19.12.2024", "Jana Pawła II, Łódź", "29", "30", "2", "-1")
                ColoredBox(colorFlag = "Umiarkowany", "01.12.2024", "Jana Pawła II, Łódź", "33", "30", "2", "-1")
                ColoredBox(colorFlag = "Dostateczny", "05.12.2024", "Jana Pawła II, Łódź", "25", "30", "2", "-1")
                ColoredBox(colorFlag = "Zły", "05.12.2024", "Jana Pawła II, Łódź", "25", "30", "2", "-1")
                ColoredBox(colorFlag = "Bardzo_Zły", "05.12.2024", "Jana Pawła II, Łódź", "25", "30", "2", "-1")
                ColoredBox(colorFlag = "", "05.12.2024", "Jana Pawła II, Łódź", "25", "30", "2", "-1")
            }

            // Dolna maska
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

        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 120.dp)
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
    }

    NavMenu(selectedIndex.value) { index ->
        selectedIndex.value = index
    }
}

@Composable
fun ColoredBox(colorFlag: String, date: String, place: String, valuePM25: String, valuePM10: String, valueNO2: String, valueSO2: String) {
    val flagColor = when (colorFlag) {
        "Bardzo_Dobry" -> Color(IndexColors.Bardzo_Dobry.rgb)
        "Dobry" -> Color(IndexColors.Dobry.rgb)
        "Umiarkowany" -> Color(IndexColors.Umiarkowany.rgb)
        "Dostateczny" -> Color(IndexColors.Dostateczny.rgb)
        "Zły" -> Color(IndexColors.Zły.rgb)
        "Bardzo_Zły" -> Color(IndexColors.Bardzo_Zły.rgb)
        else -> Color(IndexColors.Brak.rgb)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp) // Zwiększona wysokość, aby pomieścić dwa rzędy tekstu
            .background(Color(0xFF80E4FF), shape = RoundedCornerShape(8.dp))
    ) {
        // Kolumna z tekstem i obrazem
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = date,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp, top = 4.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F)
                )
                Text(
                    text = place,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp, top = 4.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F)
                )
                Button(
                    onClick = { /* Akcja przycisku */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(26.dp), // Rozmiar przycisku (możesz dostosować według potrzeb)
                    contentPadding = PaddingValues(0.dp), // Usuń domyślne paddingi przycisku
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Ustaw przezroczyste tło, jeśli potrzebne
                ) {
                    Image(
                        painter = painterResource(R.drawable.button_close),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp) // Rozmiar ikony (możesz dostosować według potrzeb)
                    )
                }

            }
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PM 2.5 [$valuePM25] PM 10 [$valuePM10] \nNO 2 [$valueNO2] SO 2 [$valueSO2]",
                    modifier = Modifier
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.prompt)),
                    fontWeight = FontWeight(250),
                    color = Color(0xFF3F3F3F)
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(flagColor, shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}



@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    HistoryView()
}
