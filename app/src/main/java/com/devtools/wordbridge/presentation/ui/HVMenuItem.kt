package com.devtools.wordbridge.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.devtools.wordbridge.R
import com.devtools.wordbridge.presentation.ui.navigation.NavItem
import com.devtools.wordbridge.presentation.ui.navigation.toMenuItem
import java.util.UUID

/*
data class HVDropDownMenuItem(
    val route: String,
    val label: String,
    //val icon: ImageVector? = null,
    @DrawableRes val icon: Int? = null,
    val iconTint: Color = Color.White,
    val backgroundColor: Color = Color.Transparent,
    val selectedColor: Color = Color.Blue.copy(alpha = 0.3f),
    val onClick: () -> Unit = {}
)
*/

sealed class HVMenuItem(
    val id: String = UUID.randomUUID().toString(),   // unique id
    val route: String,
    val label: String,
    //val icon: ImageVector? = null,
    @DrawableRes val icon: Int? = null,
    val iconTint: Color = Color.White,
    val backgroundColor: Color = Color.Transparent,
    val selectedColor: Color = Color.Blue.copy(alpha = 0.3f),
    val onClick: () -> Unit = {},
) {
    class Option(
        route: String,
        label: String,
        icon: Int,
        iconTint: Color,
        backgroundColor: Color = Color.Transparent,
        selectedColor: Color,
        onClick: () -> Unit = {}
    ) : HVMenuItem(route = route, label = label, icon = icon, iconTint = iconTint, backgroundColor = backgroundColor, selectedColor = selectedColor, onClick = onClick)

    class DropDown(
        route: String,
        label: String,
        icon: Int,
        iconTint: Color,
        backgroundColor: Color = Color.Transparent,
        selectedColor: Color,
        onClick: () -> Unit = {}
    ) : HVMenuItem(route = route, label = label, icon = icon, iconTint = iconTint, backgroundColor = backgroundColor, selectedColor = selectedColor, onClick = onClick)

}


/** Option manu */
val optionMenu = listOf(
    HVMenuItem.Option(route = "", label = "Archive", icon = R.drawable.ic_selected_add, iconTint = Color(0xFF81C784), selectedColor = Color(0xFF81C784)),
    HVMenuItem.Option(route = "", label = "Edit", icon = R.drawable.ic_selected_add, iconTint = Color(0xFFE57373), selectedColor = Color(0xFFE57373)),
    HVMenuItem.Option(route = "", label = "Delete", icon = R.drawable.ic_selected_favorite, iconTint = Color(0xFF4FC3F7), selectedColor = Color(0xFF4FC3F7)),
    HVMenuItem.Option(route = "", label = "Favorite", icon = R.drawable.ic_selected_setting, iconTint = Color(0xFF4DB6AC), selectedColor = Color(0xFF4DB6AC)),
    HVMenuItem.Option(route = "", label = "Export", icon = R.drawable.ic_selected_setting, iconTint = Color(0xFFBA68C8), selectedColor = Color(0xFFBA68C8)),
//    HVMenuItem.Option(route = "", label = "Item_6", icon = R.drawable.ic_selected_add, iconTint = Color(0xFFFF8A65), selectedColor = Color(0xFFFF8A65)),
    )

/** Top manu */
val navItems = listOf(
    NavItem.WordAdd,
    NavItem.Favorite,
    NavItem.Settings,
    NavItem.WidgetSetting
)

val dropDownMenuItems = navItems.mapIndexed { index, navItem ->
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

