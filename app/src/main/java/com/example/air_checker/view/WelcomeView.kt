package com.example.air_checker.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toIntSize
import com.example.air_checker.R

class WelcomeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            val text = if(intent.getStringExtra("buttonText") != null) intent.getStringExtra("buttonText") else "Zaczynajmy"
            WelcomeView(text.toString())
        }
    }


}

@Composable
fun WelcomeView(textButton: String){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val image = ImageBitmap.imageResource(id = R.drawable.background)
    Column(Modifier.statusBarsPadding().navigationBarsPadding().fillMaxSize().drawBehind { drawIntoCanvas { canvas -> drawImage(image, dstSize = size.toIntSize()) } }) {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxWidth().padding(top = screenHeight * 0.1f)
//        ){
//            Image(
//                painter = painterResource(R.drawable.logo), // TODO Resource do logo
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.size(187.dp), // Wielkość ikony
//            )
//        }
        Spacer(Modifier.height(180.dp))
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Aplikacja pozwala zorientować ci się, jakiej jakości powietrze znajduje się wokół ciebie.\n", color = Color.White)
            Text(text = "Prezentuje Krajowy Indeks Jakości Powietrza oraz informacje o kluczowych wskaźnikach jakości powietrza:\n", color = Color.White)
            Text(text = "• PM 2.5 – drobne pyły zawieszone (średnica < 2,5 μm)", color = Color.White)
            Text(text = "• PM 10 – większe pyły zawieszone (średnica < 10 μm)", color = Color.White)
            Text(text = "• NO₂ (dwutlenek azotu)", color = Color.White)
            Text(text = "• SO₂ (dwutlenek siarki)", color = Color.White)
            Text(text = "• O₃ (ozon)", color = Color.White)
        }
        val context = LocalContext.current
        FloatingActionButton(
            containerColor = Color(0xFF80E4FF),
            contentColor = Color.White,
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = screenWidth * 0.15f).padding(top = screenHeight * 0.3f)
        ){
            Text(
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                text = textButton
            )
        }
    }
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
fun WelcomePreview(){
    WelcomeView("Zaczynajmy")
}

