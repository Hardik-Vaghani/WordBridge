package com.devtools.wordbridge.presentation.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.devtools.wordbridge.presentation.settings.SettingsScreen
import com.devtools.wordbridge.presentation.ui.theme.ColorBottomBarBackground
import com.devtools.wordbridge.presentation.ui.theme.ColorBottomBarIndicatorColor
import com.devtools.wordbridge.presentation.ui.theme.ColorBottomBarSelectedItemLabel
import com.devtools.wordbridge.presentation.ui.theme.ColorBottomBarUnselectedItemLabel
import com.devtools.wordbridge.presentation.widget_settings_screen.WidgetSettingsScreen
import com.devtools.wordbridge.presentation.word_add.WordAddScreen
import com.devtools.wordbridge.presentation.word_list.WordScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavHostWithDefault() {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Words, BottomNavItem.Settings)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var bottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing), initialOffsetY = { it } // slide from bottom
                ) + fadeIn(animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), targetOffsetY = { it } // slide down
                ) + fadeOut(animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing))
            )
            {
                NavigationBar(
                    modifier = Modifier
                        .padding(start= 12.dp, end = 12.dp) // add margin from edges
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp)), // round only top corners, // you can add padding/clip here
                    containerColor = ColorBottomBarBackground, // transparent background
                    tonalElevation = 0.dp, // removes shadow
                ) {
                    items.forEach { item ->
                        val selected = currentRoute == item.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
//                                Surface(
//                                    shape = RoundedCornerShape(12.dp),
//                                    color = if (selected) ColorBottomBarSelectedItemBackground.copy(alpha = 0.2f)
//                                    else Color.Transparent,
//                                ) {}
                                val iconRes = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = item.label,
                                    tint = Color.Unspecified // keeps original drawable color
                                )
                            },
                            label = {
                                Text(item.label,
                                    //color = if (currentRoute == item.route) ColorSelected else ColorUnselected
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                //selectedIconColor = Color.White, unselectedIconColor = Color.Gray,
                                selectedTextColor = ColorBottomBarSelectedItemLabel, unselectedTextColor = ColorBottomBarUnselectedItemLabel,
                                indicatorColor = ColorBottomBarIndicatorColor // the background for selected item
                            ),
                        )
                    }
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavItem.Words.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Words.route) {
                WordScreen(
                    onBottomBarVisibilityChange = { isScrollingDown ->
                        bottomBarVisible = !isScrollingDown
                    }
                )
            }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
        }
    }
}

/*
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val primaryBottomItems = listOf(BottomNavItem.Words, BottomNavItem.Settings)
    val secondaryBottomItems = listOf(BottomNavItem.WordAdd, BottomNavItem.WidgetSetting)
    var bottomBarVisible by remember { mutableStateOf(true) }
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.BottomStart) {
            NavHost(navController, startDestination = BottomNavItem.Words.route) {
                composable(BottomNavItem.Words.route) { WordScreen { visible -> bottomBarVisible = visible } }
                composable(BottomNavItem.Settings.route) { SettingsScreen() }
//                composable(BottomNavItem.WordAdd.route) { WordAddScreen() }
//                composable(BottomNavItem.WidgetSetting.route) { WidgetSettingsScreen() }
            }
            FloatingBottomBar(
                items = primaryBottomItems,
                currentRoute = currentRoute?.destination?.route,
                onItemClick = { item -> navController.navigate(item.route) { launchSingleTop = true; restoreState = true } },
                visible = bottomBarVisible
            )
        }
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.BottomEnd) {
            NavHost(navController, startDestination = BottomNavItem.Words.route) {
                composable(BottomNavItem.WordAdd.route) { WordAddScreen() }
                composable(BottomNavItem.WidgetSetting.route) { WidgetSettingsScreen() }
            }
            FloatingBottomBar1(
                items = primaryBottomItems,
                currentRoute = currentRoute?.destination?.route,
                onItemClick = { item -> navController.navigate(item.route) { launchSingleTop = true; restoreState = true } },
                visible = bottomBarVisible
            )
        }
    }
}
*/

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val primaryBottomItems = listOf(BottomNavItem.Words, BottomNavItem.Settings)
    val secondaryBottomItems = listOf(BottomNavItem.WordAdd, BottomNavItem.WidgetSetting)
    var bottomBarVisible by remember { mutableStateOf(true) }
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ---- NavHost (Single) ----
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Words.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(BottomNavItem.Words.route) {
                    WordScreen { visible -> bottomBarVisible = visible }
                }
                composable(BottomNavItem.Settings.route) { SettingsScreen() }
                composable(BottomNavItem.WordAdd.route) { WordAddScreen() }
                composable(BottomNavItem.WidgetSetting.route) { WidgetSettingsScreen() }
            }

            // ---- Bottom Bars in ONE Row ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp) // spacing from bottom
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (currentRoute?.destination?.route) {
                    in listOf(BottomNavItem.Words.route, BottomNavItem.Settings.route) -> {
                        FloatingBottomBar(
                            items = primaryBottomItems,
                            currentRoute = currentRoute?.destination?.route,
                            onItemClick = { item ->
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                            },
                            visible = bottomBarVisible
                        )
                    }

                    in listOf(BottomNavItem.WordAdd.route, BottomNavItem.WidgetSetting.route) -> {
                        FloatingBottomBar1(
                            items = secondaryBottomItems,
                            currentRoute = currentRoute?.destination?.route,
                            onItemClick = { item ->
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                            },
                            visible = bottomBarVisible
                        )
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatingBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(250, easing = LinearOutSlowInEasing)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) + fadeOut(tween(200, easing = LinearOutSlowInEasing)),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        val navBarHeight = 80.dp
        val navBarWidth = 208.dp //navBarHeight * 2.6f
        val itemHeight = 44.dp // approximate default height of NavigationBarItem content

        val verticalOffset = 26.dp
        NavigationBar(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(navBarHeight)
                .width(navBarWidth)
                .clip(RoundedCornerShape( 24.dp)),
            containerColor = ColorBottomBarBackground,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    modifier = Modifier.height(itemHeight).offset( y = verticalOffset),
                    selected = selected,
                    onClick = { onItemClick(item) },
                    icon = {
                        val iconRes = if (selected) item.selectedIcon else item.unselectedIcon
                        Icon(painter = painterResource(id = iconRes), contentDescription = item.label)
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = ColorBottomBarSelectedItemLabel,
                        unselectedTextColor = ColorBottomBarUnselectedItemLabel,
                        indicatorColor = ColorBottomBarIndicatorColor
                    )
                )
            }
        }
    }
}

@Composable
fun RememberBottomBarScrollState(
    listState: LazyListState,
    threshold: Int = 20,
    onVisibilityChange: (Boolean) -> Unit
) {
    var lastPosition by remember { mutableStateOf(0) }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val current = index * 1000 + offset
                if (current > lastPosition + threshold) onVisibilityChange(false)// scrolling up → hide bottom bar
                else if (current < lastPosition - threshold) onVisibilityChange(true)// scrolling down → show bottom bar
                lastPosition = current
            }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FloatingBottomBar1(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(250, easing = LinearOutSlowInEasing)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) + fadeOut(tween(200, easing = LinearOutSlowInEasing)),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        val navBarHeight = 80.dp
        val navBarWidth = 208.dp //navBarHeight * 2.6f
        val itemHeight = 44.dp // approximate default height of NavigationBarItem content

        val verticalOffset = 26.dp
        NavigationBar(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(navBarHeight)
                .width(navBarWidth)
                .clip(RoundedCornerShape( 24.dp)),
            containerColor = ColorBottomBarBackground,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    modifier = Modifier.height(itemHeight).offset( y = verticalOffset),
                    selected = selected,
                    onClick = { onItemClick(item) },
                    icon = {
                        val iconRes = if (selected) item.selectedIcon else item.unselectedIcon
                        Icon(painter = painterResource(id = iconRes), contentDescription = item.label)
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = ColorBottomBarSelectedItemLabel,
                        unselectedTextColor = ColorBottomBarUnselectedItemLabel,
                        indicatorColor = ColorBottomBarIndicatorColor
                    )
                )
            }
        }
    }
}

