package com.devtools.wordbridge.presentation.ui.custom_ui

import androidx.annotation.DrawableRes
import com.devtools.wordbridge.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.devtools.wordbridge.presentation.ui.theme.ColorDividerSeparator_1
import com.devtools.wordbridge.presentation.ui.theme.ColorDividerSeparator_2
import com.devtools.wordbridge.presentation.ui.theme.enter_03
import com.devtools.wordbridge.presentation.ui.theme.exit_03
import kotlinx.coroutines.delay

data class CustomMenuItem(
    val route: String,
    val label: String,
    //val icon: ImageVector? = null,
    @DrawableRes val icon: Int? = null,
    val iconTint: Color = Color.White,
    val labelColor: Color = Color.White,
    val backgroundColor: Color = Color.Transparent,
    val selectedColor: Color = Color.Blue.copy(alpha = 0.3f),
    val onClick: () -> Unit = {}
)

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    anchorBounds: IntRect?,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    items: List<CustomMenuItem>,
    menuBackgroundColor: Color = Color(0xFF2C3E50),
    menuCornerRadius: Dp = 12.dp,
    menuElevation: Dp = 8.dp,
    onItemClick: (CustomMenuItem) -> Unit
) {
//    if (!isOpen || anchorBounds == null) return
    if (anchorBounds == null) return

    var showMenu by remember { mutableStateOf(isOpen) }

    // Update local state when isOpen changes
    LaunchedEffect(isOpen) {
        if (isOpen) {
            showMenu = true
        } else {
            // Wait for exit animation duration
            delay(300) // match your exit animation duration
            showMenu = false
        }
    }

    if (!showMenu) return

    val density = LocalDensity.current
    val offsetX = anchorBounds.left
    val offsetY = anchorBounds.bottom + with(density) { 8.dp.toPx() }.toInt()


    Popup(
        onDismissRequest = onDismissRequest,
        offset = IntOffset(offsetX, offsetY),
        alignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isOpen,
            enter = enter_03,
            exit = exit_03
        ) {
            Surface(
                modifier = modifier
                    .padding(horizontal = 80.dp, vertical = 8.dp)
                    .widthIn(min = 180.dp)
                    .shadow(elevation = menuElevation, shape = RoundedCornerShape(menuCornerRadius))
                    .clip(RoundedCornerShape(menuCornerRadius))
                    .background(menuBackgroundColor),
                shape = RoundedCornerShape(menuCornerRadius),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(modifier = Modifier.background(color = menuBackgroundColor)) {
                    items.forEachIndexed { index, item ->
                        val delay = index * 60
                        AnimatedItem(
                            delayMillis = delay,
                            item = item,
                            showDivider = index != items.lastIndex,
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
    item: CustomMenuItem,
    showDivider: Boolean,
    animationDuration: Int = 300,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = enter_03,
        exit = exit_03

    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null//rememberRipple(bounded = true)
                    ) { onClick() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.icon != null) {
                    Icon(
                        painter = painterResource(id = item.icon!!),//item.icon,
                        contentDescription = null, tint = item.iconTint)
                    Spacer(Modifier.width(12.dp))
                }
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = item.selectedColor
                )
            }

            // dotted separator
            if (showDivider) {
                DottedHorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ColorDividerSeparator_2.copy(alpha = 0.5f),
                    thickness = 0.7.dp,
                    dashLength = 6f,
                    gapLength = 6f
                )
            }
        }
    }
}

val myItemsList = listOf(
//    CustomMenuItem(route = "words", label = "Words", labelColor = Color.Red, icon = Icons.Default.Settings, iconTint = Color(0xFF81C784), selectedColor = Color(0xFF81C784)),
    CustomMenuItem(route = "word_add", label = "Add", labelColor = Color.Green, icon = R.drawable.ic_selected_add, iconTint = Color(0xFFE57373), selectedColor = Color(0xFFE57373)),
    CustomMenuItem(route = "favorite", label = "Favorite", labelColor = Color.Blue, icon = R.drawable.ic_selected_favorite, iconTint = Color(0xFF4FC3F7), selectedColor = Color(0xFF4FC3F7)),
    CustomMenuItem(route = "settings", label = "Settings", labelColor = Color.Yellow, icon = R.drawable.ic_selected_setting, iconTint = Color(0xFF4DB6AC), selectedColor = Color(0xFF4DB6AC)),
    CustomMenuItem(route = "widget_setting", label = "Widget", labelColor = Color.Magenta, icon = R.drawable.ic_selected_setting, iconTint = Color(0xFFBA68C8), selectedColor = Color(0xFFBA68C8)),
//    CustomMenuItem(route = "Item 6", label = "Item 6", labelColor = Color.Cyan, icon = Icons.Default.Settings, iconTint = Color(0xFFFF8A65), selectedColor = Color(0xFFFF8A65)),

)
fun Rect.toIntRect(): IntRect = IntRect(
    left = this.left.toInt(),
    top = this.top.toInt(),
    right = this.right.toInt(),
    bottom = this.bottom.toInt()
)