package com.devtools.wordbridge.presentation.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devtools.wordbridge.R
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder

@Composable
fun SettingsScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text("Settings", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)

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
                            .border(width = 1.dp, color = ColorIconBorderUnselectedItem, shape = RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(ColorIconBorderUnselectedItem)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("• Dark Mode (TODO)")
        Text("• Notifications (TODO)")
    }
}
