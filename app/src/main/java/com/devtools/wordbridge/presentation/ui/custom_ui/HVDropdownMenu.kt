package com.devtools.wordbridge.presentation.ui.custom_ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.devtools.wordbridge.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.devtools.wordbridge.presentation.ui.navigation.NavItem
import com.devtools.wordbridge.presentation.ui.navigation.toMenuItem
import com.devtools.wordbridge.presentation.ui.theme.ColorDividerSeparator_2
import com.devtools.wordbridge.presentation.ui.theme.animationPairBouncySlideMenu
import com.devtools.wordbridge.presentation.ui.theme.enterScaleMediumBounce
import com.devtools.wordbridge.presentation.ui.theme.exitScaleMediumBounce
import kotlinx.coroutines.delay

data class CustomMenuItem(
    val route: String,
    val label: String,
    //val icon: ImageVector? = null,
    @DrawableRes val icon: Int? = null,
    val iconTint: Color = Color.White,
    val backgroundColor: Color = Color.Transparent,
    val selectedColor: Color = Color.Blue.copy(alpha = 0.3f),
    val onClick: () -> Unit = {}
)

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HVDropdownMenu(
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val surfaceWidth = screenWidth * 0.75f // 3/4 of screen width

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
    // Full-screen box so menu floats over content

    // Calculate vertical offset from anchor (optional)
    val density = LocalDensity.current
    val topPadding = with(density) { (anchorBounds.bottom + 16.dp.toPx()).toDp() }

    Box(
        modifier = modifier
            .wrapContentWidth()
            .padding(top = topPadding), // distance from bottom
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isOpen,
            enter = animationPairBouncySlideMenu.first,
            exit = animationPairBouncySlideMenu.second
        ) {
            Surface(
                modifier = modifier
                    .width(surfaceWidth)
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
    /*val density = LocalDensity.current
    val offsetX = anchorBounds.left
    val offsetY = anchorBounds.bottom // anchorBounds.bottom + with(density) { 8.dp.toPx() }.toInt()


    Popup(
        onDismissRequest = onDismissRequest,
        offset = IntOffset(offsetX, offsetY),
        alignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isOpen,
            enter = animationPairBouncySlideMenu.first,
            exit = animationPairBouncySlideMenu.second
        ) {
            Surface(
                modifier = modifier
                    //.padding(horizontal = 80.dp, vertical = 8.dp)
                    .padding(end = (screenWidth - surfaceWidth)/2)
                    .width(surfaceWidth)
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
    }*/
}

@Composable
private fun AnimatedItem(
    delayMillis: Int,
    item: CustomMenuItem,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterScaleMediumBounce,
        exit = exitScaleMediumBounce

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

/** Option manu */
val optionMenu = listOf(
    CustomMenuItem(route = "", label = "Archive", icon = R.drawable.ic_selected_add, iconTint = Color(0xFF81C784), selectedColor = Color(0xFF81C784)),
    CustomMenuItem(route = "", label = "Edit", icon = R.drawable.ic_selected_add, iconTint = Color(0xFFE57373), selectedColor = Color(0xFFE57373)),
    CustomMenuItem(route = "", label = "Delete", icon = R.drawable.ic_selected_favorite, iconTint = Color(0xFF4FC3F7), selectedColor = Color(0xFF4FC3F7)),
    CustomMenuItem(route = "", label = "Favorite", icon = R.drawable.ic_selected_setting, iconTint = Color(0xFF4DB6AC), selectedColor = Color(0xFF4DB6AC)),
    CustomMenuItem(route = "", label = "Export", icon = R.drawable.ic_selected_setting, iconTint = Color(0xFFBA68C8), selectedColor = Color(0xFFBA68C8)),
    CustomMenuItem(route = "", label = "Item_6", icon = R.drawable.ic_selected_add, iconTint = Color(0xFFFF8A65), selectedColor = Color(0xFFFF8A65)),

)

/** Top manu */
val navItems = listOf(
    NavItem.WordAdd,
    NavItem.Favorite,
    NavItem.Settings,
    NavItem.WidgetSetting
)

val menuItems = navItems.mapIndexed { index, navItem ->
    navItem.toMenuItem(
        iconTint = when (index) {
            0 -> Color(0xFFE57373)//Color(0xFF81C784),Color(0xFFFF8A65)
            1 -> Color(0xFF4FC3F7)
            2 -> Color(0xFF4DB6AC)
            else -> Color(0xFFBA68C8)
        },
        selectedColor = when (index) {
            0 -> Color(0xFFE57373)
            1 -> Color(0xFF4FC3F7)
            2 -> Color(0xFF4DB6AC)
            else -> Color(0xFFBA68C8)
        },
        onClick = { /* handle click here */ }
    )
}

fun Rect.toIntRect(): IntRect = IntRect(
    left = this.left.toInt(),
    top = this.top.toInt(),
    right = this.right.toInt(),
    bottom = this.bottom.toInt()
)