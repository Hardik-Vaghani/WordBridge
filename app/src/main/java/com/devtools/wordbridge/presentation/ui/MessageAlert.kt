package com.devtools.wordbridge.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtools.wordbridge.presentation.ui.theme.ColorMessageAlertBackground
import kotlinx.coroutines.delay

@Composable
fun MessageAlert(
    message: String?,
    onDismiss: () -> Unit,
    backgroundColor: Color = ColorMessageAlertBackground,
    textColor: Color = Color.White
)  {
    LaunchedEffect(message) {
        if (message != null) {
            delay(2000) // show for 2 seconds
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = message != null,
        enter = scaleIn(
            initialScale = 0.8f, // starts smaller
            animationSpec = tween(durationMillis = 300),
            transformOrigin = TransformOrigin(0.5f, 1f) // 👈 center bottom
        ),
        exit = scaleOut(
            targetScale = 0.0f, // fully collapsed
            animationSpec = tween(durationMillis = 200),
            transformOrigin = TransformOrigin(0.5f, 1f), // 👈 center bottom
        )
    ) {
        Box(
            modifier = Modifier.background(color = Color.Transparent)
                .fillMaxSize().padding(bottom = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = backgroundColor.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message ?: "",
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}
