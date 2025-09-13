package com.devtools.wordbridge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devtools.wordbridge.presentation.ui.navigation.AppNavHost
import com.devtools.wordbridge.presentation.ui.theme.WordBridgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordBridgeTheme {
                    AppNavHost()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> WordScreen(modifier = Modifier.padding(innerPadding)) }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordBridgeTheme {
        Greeting("Android")
    }
}