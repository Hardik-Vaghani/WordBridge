package com.devtools.wordbridge.presentation.ui.custom_ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.*           // Box, Image, ScrollableColumn, background, border, clickable, etc.
import androidx.compose.foundation.layout.*    // Row, Column, Box, Spacer, Padding, Arrangement, AlignmentLine, etc.
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*     // RoundedCornerShape, CircleShape, CutCornerShape, etc.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*            // MaterialTheme, Text, Icon, Button, Surface, Scaffold, etc.
import androidx.compose.runtime.*              // Composable, remember, mutableStateOf, LaunchedEffect, derivedStateOf, etc.
import androidx.compose.ui.*                   // Modifier, Alignment, LayoutDirection, graphicsLayer, windowInfo, etc.
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*              // drawBehind, clip, shadow, drawWithContent, etc.
import androidx.compose.ui.graphics.*          // Color, Brush, Path, ImageBitmap, Painter, ShaderBrush, etc.
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.*               // painterResource, stringResource, colorResource, dimensionResource, etc.
import androidx.compose.ui.text.*              // AnnotatedString, SpanStyle, TextStyle, buildAnnotatedString, etc.
import androidx.compose.ui.text.font.*         // Font, FontFamily, FontWeight, FontStyle, etc.
import androidx.compose.ui.unit.*              // Dp, dp, Sp, TextUnit, Constraints, IntOffset, IntSize, etc.




@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FloatingOptionMenu(
    items: List<CustomMenuItem>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF44336),
    cornerRadius: Dp = 12.dp,
    elevation: Dp = 8.dp,
    onItemClick: (CustomMenuItem) -> Unit
) {
    // Full-screen box so menu floats over content]
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val surfaceWidth = screenWidth * 0.75f // 3/4 of screen width
    Box(
        modifier = modifier
            .wrapContentWidth()
            .padding(bottom = 24.dp), // distance from bottom
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = modifier.width(surfaceWidth).widthIn(min = 180.dp)
                .shadow(elevation = elevation, shape = RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = backgroundColor
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.spacedBy(16.dp), // custom spacing between items
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items.flatMap { listOf(it, it, it) }) { item ->
                    Column(
                        modifier = Modifier
                            .widthIn(min = 64.dp)
                            .clickable { onItemClick(item) }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icon
                        item.icon?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = item.label,
                                tint = item.iconTint,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Label
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = item.selectedColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun BottomActionBar(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    actions: List<ActionItem>,
    modifier: Modifier = Modifier,
    onActionClick: (ActionItem) -> Unit
) {
    val transition = updateTransition(targetState = isExpanded, label = "bottomBarTransition")

    val barHeight by transition.animateDp(label = "barHeight") {
        if (it) 72.dp else 56.dp
    }

    val barWidth by transition.animateDp(label = "barWidth") {
        if (it) Dp.Unspecified else 56.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
            contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .then(
                    if (isExpanded)
                        Modifier.fillMaxWidth()
                    else
                        Modifier.size(56.dp)
                )
                .clickable { if (!isExpanded) onToggle() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            if (isExpanded) {
                // Horizontal list of actions
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(actions) { action ->
                        ActionButton(action = action, onClick = { onActionClick(action) })
                        Spacer(Modifier.width(8.dp))
                    }
                    item {
                        IconButton(onClick = onToggle) {
                            Icon(Icons.Default.Close, contentDescription = "Close menu")
                        }
                    }
                }
            } else {
                // Collapsed floating button
                IconButton(onClick = onToggle, modifier = Modifier.size(56.dp)) {
                    Icon(Icons.Default.Menu, contentDescription = "Open menu")
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    action: ActionItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.label,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = action.label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

data class ActionItem(
    val icon: ImageVector,
    val label: String,
    val route: String? = null, // optional for navigation
    val onAction: (() -> Unit)? = null // optional for VM calls
)
