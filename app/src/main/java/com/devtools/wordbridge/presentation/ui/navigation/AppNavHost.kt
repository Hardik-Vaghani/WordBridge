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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devtools.wordbridge.presentation.screen.favorite.FavoriteScreen
import com.devtools.wordbridge.presentation.screen.settings.SettingsScreen
import com.devtools.wordbridge.presentation.screen.widget_settings.WidgetSettingsScreen
import com.devtools.wordbridge.presentation.screen.word_add.WordAddScreen
import com.devtools.wordbridge.presentation.screen.word_list.WordScreen
import com.devtools.wordbridge.presentation.screen.word_list.WordViewModel
import com.devtools.wordbridge.presentation.screen.word_update.WordUpdateScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var onScrollVisibleOption by remember { mutableStateOf(true) }
    val currentRoute by navController.currentBackStackEntryAsState()
    val wordViewModel: WordViewModel = hiltViewModel()

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
                        },
                        navController = navController,
                        viewModel = wordViewModel
                    )
                }
                composable(NavItem.WordAdd.route) {
                    WordAddScreen(
                        onWordSaved = {},
                        onBackClicked = {navController.popBackStack() }
                    )
                }
                composable(
                    NavItem.WordUpdate.route,
                    arguments = listOf(
                    navArgument("wordId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )) { backStackEntry ->
                    val wordId = backStackEntry.arguments?.getInt("wordId") ?: 0
                    WordUpdateScreen(
                        wordId = wordId,
                        onWordUpdated = { navController.popBackStack() },
                        onBackClicked = { navController.popBackStack() },
                    )
                }
                composable(NavItem.WidgetSetting.route) { WidgetSettingsScreen() { navController.popBackStack() } }
                composable(NavItem.Settings.route) { SettingsScreen() { navController.popBackStack() } }
                composable(NavItem.Favorite.route) {
                    FavoriteScreen(
                        onBackClicked = { navController.popBackStack() },
                        navController = navController,
                        viewModel = wordViewModel
                    )
                }
            }
        }
    }

}


@Composable
fun RememberScrollState(
    listState: LazyListState,
    threshold: Int = 20,
    autoHideDelay: Long = 2500L, // delay after showing
    onVisibilityChange: (visible: Boolean) -> Unit = {},
    onScrollDirection: (isScrollingDown: Boolean) -> Unit = {},
    onScroll: ((firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) -> Unit)? = null,
) {
    var lastPosition by remember { mutableStateOf(0) }
    var job by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val current = index * 1000 + offset // combine index + offset for position

                // 1️⃣ Detect scroll direction
                if (current > lastPosition + threshold) {
                    // Scrolling down (content moves up)
                    onScrollDirection(false)
                    onVisibilityChange(false) // auto-hide
                    job?.cancel()
                } else if (current < lastPosition - threshold) {
                    // Scrolling up (content moves down)
                    onScrollDirection(true)
                    onVisibilityChange(true) // show

                    job?.cancel()
                    job = scope.launch {
                        delay(autoHideDelay)
                        onVisibilityChange(false) // auto-hide after delay
                    }
                }

                lastPosition = current

                // 2️⃣ Scroll callback with current index/offset
                onScroll?.invoke(index, offset)
            }
    }
}
