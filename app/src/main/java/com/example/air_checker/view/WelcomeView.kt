package com.example.air_checker.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Column(Modifier.statusBarsPadding().navigationBarsPadding().fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(top = screenHeight * 0.1f)
        ){
            Image(
                painter = painterResource(R.drawable.info_blue), // TODO Resource do logo
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(187.dp), // Wielkość ikony
            )
        }
        Text(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
            textAlign = TextAlign.Center,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sed maximus nisi. In felis ante, commodo in mi in, eleifend sodales dui. Suspendisse vestibulum turpis vel dapibus blandit.",
            modifier = Modifier.padding(horizontal = 15.dp).padding(top = screenHeight * 0.2f)
            )
        val context = LocalContext.current
        FloatingActionButton(
            containerColor = Color(0xFF80E4FF),
            contentColor = Color.White,
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = screenWidth * 0.15f).padding(bottom = 15.dp).padding(top = screenHeight * 0.2f)
        ){
            Text(
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.prompt, FontWeight.Normal)),
                text = textButton
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview(){
    WelcomeView("Zaczynajmy")
}

