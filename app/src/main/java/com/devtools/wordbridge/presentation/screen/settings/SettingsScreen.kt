package com.devtools.wordbridge.presentation.screen.settings

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devtools.wordbridge.R
import com.devtools.wordbridge.presentation.ui.theme.colorIconBorderDeactivate
import com.devtools.wordbridge.presentation.ui.theme.colorOutlinedTextBorder

@Composable
fun SettingsScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text("Settings", color = colorOutlinedTextBorder(), style = MaterialTheme.typography.headlineMedium)

            Row(modifier = Modifier.fillMaxWidth().height(33.dp), horizontalArrangement = Arrangement.End) {

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { onBackClicked()}
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        //                        painter = rememberVectorPainter(image = Icons.Default.Search),
                        contentDescription = "Back button",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(color = Color.Transparent)
                            .border(width = 1.dp, color = colorIconBorderDeactivate(), shape = RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(colorIconBorderDeactivate())
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("• Dark Mode (TODO)")


        AnimatedBoxScreen()

    }
}
@Composable
fun AnimatedBoxScreen() {
    var expanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(if (expanded) 200.dp else 100.dp, tween(500))
    Box(
        modifier = Modifier
            .size(width = size, height = size/2)
            .background(Color.Magenta, shape = RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded },
        contentAlignment = Alignment.Center
    ) {
        Text("Click Me", color = Color.White,
//            modifier = Modifier.then(if (expanded) Modifier.padding(64.dp) else Modifier.padding(16.dp))
            )
    }
}



