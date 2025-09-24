package com.devtools.wordbridge.presentation.ui.custom_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    dotSpacing: Dp = 4.dp,
    dotRadius: Dp = 1.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(dotRadius * 2)
    ) {
        val width = size.width
        val spacingPx = dotSpacing.toPx()
        val radiusPx = dotRadius.toPx()
        var x = 0f
        val centerY = size.height / 2f
        while (x < width) {
            drawCircle(
                color = color,
                radius = radiusPx,
                center = Offset(x, centerY)
            )
            x += spacingPx
        }
    }
}
@Composable
fun DottedHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    thickness: Dp = 1.dp,
    dashLength: Float = 10f,
    gapLength: Float = 10f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength))
        )
    }
}
