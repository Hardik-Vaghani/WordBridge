package com.devtools.wordbridge.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devtools.wordbridge.presentation.screen.favorite.FavoriteScreen
import com.devtools.wordbridge.presentation.screen.settings.SettingsScreen
import com.devtools.wordbridge.presentation.screen.widget_settings.WidgetSettingsScreen
import com.devtools.wordbridge.presentation.screen.word_add.WordAddScreen
import com.devtools.wordbridge.presentation.screen.word_list.WordScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var onScrollVisibleOption by remember { mutableStateOf(true) }
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(contentWindowInsets = WindowInsets(0)) { innerPadding : PaddingValues->
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(color = Color(0xFFFF9800))
                .padding(innerPadding)
        ) {
            // ---- NavHost (Single) ----
            NavHost(
                navController = navController,
                startDestination = NavItem.Words.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(NavItem.Words.route) {
                    WordScreen(
                        onScrollVisibilityChangeOnOption = { visible -> onScrollVisibleOption = visible },
                        onMenuNavigate = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(NavItem.WordAdd.route) { WordAddScreen (onWordSaved = {}, onBackClicked = {navController.popBackStack() }) }
                composable(NavItem.WidgetSetting.route) { WidgetSettingsScreen() { navController.popBackStack() } }
                composable(NavItem.Settings.route) { SettingsScreen() { navController.popBackStack() } }
                composable(NavItem.Favorite.route) { FavoriteScreen() { navController.popBackStack() } }
            }
        }
    }

}


@Composable
fun RememberBottomBarScrollState(
    listState: LazyListState,
    threshold: Int = 20,
    autoHideDelay: Long = 2500L, // 2.5s after showing
    onVisibilityChange: (Boolean) -> Unit
) {
    var lastPosition by remember { mutableStateOf(0) }
    var job by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val current = index * 1000 + offset

                when {
                    current > lastPosition + threshold -> {
                        // Scroll up → hide
                        onVisibilityChange(false)
                        job?.cancel()
                    }
                    current < lastPosition - threshold -> {
                        // Scroll down → show then auto-hide
                        onVisibilityChange(true)

                        job?.cancel()
                        job = scope.launch {
                            delay(autoHideDelay)
                            onVisibilityChange(false) // hide again
                        }
                    }
                }

                lastPosition = current
            }
    }
}
