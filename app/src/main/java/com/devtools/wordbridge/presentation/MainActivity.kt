package com.devtools.wordbridge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devtools.wordbridge.R
import com.devtools.wordbridge.presentation.ui.navigation.AppNavHost
import com.devtools.wordbridge.presentation.ui.theme.colorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.WordBridgeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // region Fullscreen setup
        WindowCompat.setDecorFitsSystemWindows(window, false) // Tell system not to fit content by default
        // Hide navigation bar & status bar
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        // endregion

        setContent {
            WordBridgeTheme {
                    AppNavHost()
            }
        }

    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(color = Color.Gray), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

        Text("Words", color = colorOutlinedTextBorder(), style = MaterialTheme.typography.headlineMedium)

        Row(modifier = Modifier.fillMaxWidth().height(33.dp), horizontalArrangement = Arrangement.End) {
            val buttonWidth = 48.dp
            val buttonHeight = 36.dp // 3:4 aspect ratio
            val corner = 6.dp        // subtle rounding

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { /* on click */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_option),
                    contentDescription = "My button",
                    modifier = Modifier.size(32.dp) // image size inside button
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {  },
                modifier = Modifier
                    .wrapContentHeight()
                    .width(70.dp)
                    .padding(vertical = 4.dp, ) // padding inside button
                    .background(
                        color = Color.Transparent, // or any fill color
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 0.5.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = colorOutlinedTextBorder(),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Menu icon"
                )
            }
        }
    }
//    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordBridgeTheme {
        Greeting("Android")
    }
}