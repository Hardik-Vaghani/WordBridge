package com.devtools.wordbridge.presentation.ui.custom_ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.*           // Box, Image, ScrollableColumn, background, border, clickable, etc.
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*    // Row, Column, Box, Spacer, Padding, Arrangement, AlignmentLine, etc.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*     // RoundedCornerShape, CircleShape, CutCornerShape, etc.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*            // MaterialTheme, Text, Icon, Button, Surface, Scaffold, etc.
import androidx.compose.runtime.*              // Composable, remember, mutableStateOf, LaunchedEffect, derivedStateOf, etc.
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*                   // Modifier, Alignment, LayoutDirection, graphicsLayer, windowInfo, etc.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*              // drawBehind, clip, shadow, drawWithContent, etc.
import androidx.compose.ui.graphics.*          // Color, Brush, Path, ImageBitmap, Painter, ShaderBrush, etc.
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.*               // painterResource, stringResource, colorResource, dimensionResource, etc.
import androidx.compose.ui.unit.*              // Dp, dp, Sp, TextUnit, Constraints, IntOffset, IntSize, etc.
import com.devtools.wordbridge.presentation.ui.HVMenuItem
import com.devtools.wordbridge.presentation.ui.theme.colorDividerSeparator_2
import com.devtools.wordbridge.presentation.ui.theme.animationPairBouncySlideMenu
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideUpScale
import com.devtools.wordbridge.presentation.ui.theme.enterScaleMediumBounce
import com.devtools.wordbridge.presentation.ui.theme.exitScaleMediumBounce
import kotlinx.coroutines.delay


@SuppressLint("ConfigurationScreenWidthHeight", "FrequentlyChangingValue")
@Composable
fun FloatingOptionMenu(
    items: List<HVMenuItem.Option>,
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    backgroundColor: Color = Color(0xFF2C3E50),
    cornerRadius: Dp = 12.dp,
    elevation: Dp = 8.dp,
    isCollapsedOnScroll: Boolean = false, // pass true when scrolling hides menu
    onItemClick: (HVMenuItem.Option) -> Unit,
    onDismissRequest: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val minItemWidth = 64.dp       // width of a single item
    val totalItemsWidth = items.size * (minItemWidth + 8.dp + 0.5.dp) // item + padding + spacing
    val maxWidth = screenWidth * 0.75f// 3/4 of screen width
    val surfaceWidth = if (totalItemsWidth < maxWidth) { totalItemsWidth } else { maxWidth }

    val defaultVisibleCount = if (configuration.screenWidthDp >= 600) 7 else 5 // big screen 7, phone screen 5
    val firstVisibleItemsCount = lazyListState.layoutInfo.visibleItemsInfo
        .takeIf { it.isNotEmpty() }?.size
        ?.coerceAtMost(items.size)
        ?: defaultVisibleCount// default initial visible count

    Box(
        modifier = modifier
            .wrapContentWidth()
            .padding(bottom = 56.dp), // distance from bottom
        contentAlignment = Alignment.BottomCenter
    ) {
        // 🔹 The actual menu
        AnimatedVisibility(
            visible = isOpen && !isCollapsedOnScroll,
            enter = animationPairSlideUpScale.first,
            exit = animationPairBouncySlideMenu.second
        ) {
            Surface(
                modifier = modifier
                    .width(surfaceWidth)
                    .widthIn(min = 180.dp)
                    .shadow(elevation = elevation, shape = RoundedCornerShape(cornerRadius))
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                color = backgroundColor
            ) {
                LazyRow(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    horizontalArrangement = Arrangement.spacedBy(0.5.dp, alignment =  Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    itemsIndexed(items) { index, item ->
                        val shouldAnimate = index < firstVisibleItemsCount

                        AnimatedItem(
                            delayMillis = if (shouldAnimate) index * 60 else 0,
                            item = item,
                            shouldAnimate = shouldAnimate,
                            onClick = { onItemClick(item) }
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun AnimatedItem(
    delayMillis: Int,
    item: HVMenuItem.Option,
    shouldAnimate: Boolean = true,
    onClick: (HVMenuItem.Option) -> Unit
) {
    var visible by rememberSaveable(item.id) { mutableStateOf(!shouldAnimate) }

    LaunchedEffect(item.hashCode()) {
        if (shouldAnimate && !visible) {
            delay(delayMillis.toLong())
            visible = true
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterScaleMediumBounce,
        exit = exitScaleMediumBounce

    ) {

        Column(
            modifier = Modifier
                .widthIn(min = 64.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClick(item) }

                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            item.icon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = item.label,
                    tint = item.iconTint,
                    modifier = Modifier.size(32.dp).padding(top = 4.dp)
                )
            }

            // Label
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.75f,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 0.8f
                ),
                color = item.selectedColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
