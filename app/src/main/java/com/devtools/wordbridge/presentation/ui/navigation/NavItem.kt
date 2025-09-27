package com.devtools.wordbridge.presentation.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.devtools.wordbridge.R
import com.devtools.wordbridge.presentation.ui.custom_ui.CustomMenuItem


sealed class NavItem(
    val route: String,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Words : NavItem("words", "Words",
        R.drawable.ic_selected_word,     // selected
        R.drawable.ic_unselected_word     // unselected
    )
    object WordAdd : NavItem("word_add", "Add",
        R.drawable.ic_selected_add,  // selected
        R.drawable.ic_unselected_add  // unselected
    )
    object WordUpdate : NavItem("word_update/{wordId}", "Update",
        R.drawable.ic_selected_add,  // selected
        R.drawable.ic_unselected_add  // unselected
    ) {
        fun createRoute(wordId: Int) = "word_update/$wordId"
    }
    object Favorite : NavItem("favorite", "Favorite",
        R.drawable.ic_selected_favorite,  // selected
        R.drawable.ic_unselected_favorite  // unselected
    )
    object Settings : NavItem("settings", "Settings",
        R.drawable.ic_selected_setting,  // selected
        R.drawable.ic_unselected_setting  // unselected
    )
    object WidgetSetting : NavItem("widget_setting", "Widget",
        R.drawable.ic_selected_setting,  // selected
        R.drawable.ic_unselected_setting  // unselected
    )
}

fun NavItem.toMenuItem(
    iconTint: Color,
    selectedColor: Color,
    onClick: () -> Unit
) = CustomMenuItem(
    route = route,
    label = label,
    icon = selectedIcon, // or unselectedIcon depending on state
    iconTint = iconTint,
    selectedColor = selectedColor,
    onClick = onClick
)

//androidx.compose.material.icons.Icons.Default.Home