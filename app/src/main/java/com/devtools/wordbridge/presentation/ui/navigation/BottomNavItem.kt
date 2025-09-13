package com.devtools.wordbridge.presentation.ui.navigation

import androidx.annotation.DrawableRes
import com.devtools.wordbridge.R


sealed class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Words : BottomNavItem("words", "Words",
        R.drawable.ic_selected_word,     // selected
        R.drawable.ic_unselected_word     // unselected
    )
    object Settings : BottomNavItem("settings", "Settings",
        R.drawable.ic_selected_setting,  // selected
        R.drawable.ic_unselected_setting  // unselected
    )
    object WordAdd : BottomNavItem("word_add", "Add",
        R.drawable.ic_selected_setting,  // selected
        R.drawable.ic_unselected_setting  // unselected
    )
    object WidgetSetting : BottomNavItem("widget_setting", "Widget Setting",
        R.drawable.ic_selected_setting,  // selected
        R.drawable.ic_unselected_setting  // unselected
    )
}
//androidx.compose.material.icons.Icons.Default.Home