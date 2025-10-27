package com.devtools.wordbridge.presentation.screen.word_list

// presentation/wordlist/WordScreen.kt
//import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devtools.wordbridge.R
import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.core.utils.actionBarSizeDp
import com.devtools.wordbridge.core.utils.toIntRect
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.ui.custom_ui.FloatingOptionMenu
import com.devtools.wordbridge.presentation.ui.custom_ui.HVDropdownMenu
import com.devtools.wordbridge.presentation.ui.custom_ui.WordItemRow
import com.devtools.wordbridge.presentation.ui.dropDownMenuItems
import com.devtools.wordbridge.presentation.ui.navigation.RememberScrollState
import com.devtools.wordbridge.presentation.ui.optionMenu
import com.devtools.wordbridge.presentation.ui.theme.animationPairBouncyScale
import com.devtools.wordbridge.presentation.ui.theme.animationPairSlideUpDown
import com.devtools.wordbridge.presentation.ui.theme.colorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.colorOutlinedTextBorderActive
import com.devtools.wordbridge.presentation.ui.theme.colorOutlinedTextBorderDeActive
import com.devtools.wordbridge.presentation.ui.theme.appBackground
import com.devtools.wordbridge.presentation.ui.theme.appHeaderBackground
import com.devtools.wordbridge.presentation.ui.theme.enterSlideDown
import com.devtools.wordbridge.presentation.ui.theme.exitSlideUp

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    onScrollVisibilityChangeOnOption: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {},
    navController: NavController? = null,
    viewModel: WordViewModel
) {
    val words by viewModel.wordsFlow.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            // Use the provided navController or the one from parameters
            val controller = navController ?: return@LaunchedEffect
            controller.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
            viewModel.clearNavigationEvent()
        }
    }

    WordScreenContent(
        words = words,
        modifier = modifier.background(color = appBackground().copy(alpha = 0.2f)),
        onScrollVisibilityChangeOnOption = onScrollVisibilityChangeOnOption,
        onMenuNavigate = onMenuNavigate,
        action = { action -> viewModel.onAction(action) },
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun WordScreenContent(
    modifier: Modifier = Modifier,
    words: List<Word>,
    viewModel: WordViewModel? = null,
    onScrollVisibilityChangeOnOption: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {},
    action: (Action) -> Unit = {},
    navController: NavController? = null,
) {

    val listState = rememberLazyListState()
    var isFloatingOptionMenuCollapse by remember { mutableStateOf(false) }
    RememberScrollState(
        listState, autoHideDelay = 2000L,
        onVisibilityChange = {
            visible -> onScrollVisibilityChangeOnOption(visible)
//            isFloatingOptionMenuCollapse = !visible //for auto hide
                             },
        onScrollDirection = { isDown ->
            //Log.d("Scroll", "Scrolling Up? $isUp")
            isFloatingOptionMenuCollapse = !isDown //for scrolling hide
                            },
        onScroll = { index, offset -> /*Log.d("Scroll", "Index: $index Offset: $offset")*/ },
        )

    // 🔹 Search query state
    val activeColor = colorOutlinedTextBorderActive() // composable color
    val deActiveColor = colorOutlinedTextBorderDeActive() // static color

    var searchQuery by remember { mutableStateOf("") }
    val searchViewColor by remember(searchQuery) { derivedStateOf { if (searchQuery.isBlank()) deActiveColor else activeColor } }

    val selectedItemColor = colorOutlinedTextBorderActive()
    val unselectedItemColor = colorOutlinedTextBorderDeActive()

    var isSearchVisible by remember { mutableStateOf(false) }
    val searchColor by remember(isSearchVisible) { derivedStateOf { if (isSearchVisible) selectedItemColor else unselectedItemColor } }

    var isMenuOpen by remember { mutableStateOf(false) }
    val menuColor by remember(isMenuOpen) { derivedStateOf { if (isMenuOpen) selectedItemColor else unselectedItemColor } }
    var menuIconBounds by remember { mutableStateOf<IntRect?>(null) }

//    val isBarExpanded by viewModel?.isActionBarExpanded?.collectAsState()
//    val isBarExpanded = viewModel?.isActionBarExpanded?.collectAsState(initial = false)?.value ?: false
    val isSelectionActive = viewModel?.isSelectionActive?.collectAsState()?.value ?: true
    val selectedWords = viewModel?.itemSelection?.collectAsState()?.value ?: emptyList()

    // 🔹 Filter + Sort words
    val filteredWords = remember(searchQuery, words) {
        words
            .filter { word ->
                word.primaryWord.contains(searchQuery, ignoreCase = true) ||
                        word.secondaryWord.contains(searchQuery, ignoreCase = true) ||
                        word.wordMeaning.contains(searchQuery, ignoreCase = true)
            }
            .sortedBy { it.primaryWord.lowercase() } // 🔹 sort alphabetically
    }

    Box(
        modifier = modifier.fillMaxSize()
            .padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)
            .background(appBackground().copy(alpha = 0.8f) )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(60.dp)) // match your bottom bar height
            }

            items(filteredWords, key = { it.id }) { word ->
                val isExpanded = viewModel?.uiExpandCollapseState?.value?.contains(word.id) == true
                val isSelected = selectedWords.any { it.id == word.id }

                WordItemRow(
                    word = word,
                    isExpanded = isExpanded,
                    onAction =  { action -> action(action) },
                    enableSwipeStartToEnd = !isSelectionActive, // 🔹 disable swipes
                    enableSwipeEndToStart = !isSelectionActive,  // 🔹 disable swipes
                    isSelected = isSelected
                )
            }
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }
        }


        WordsHeader(
            selectedCount = selectedWords.size,
            isSearchVisible = isSearchVisible,
            onSearchToggle = { isSearchVisible = !isSearchVisible },
            onMenuToggle = { isMenuOpen = !isMenuOpen },
            onCancelSelection = { viewModel?.clearSelection() },
            onSelectAll = { viewModel?.selectAllWords() },
            searchColor = searchColor,
            menuColor = menuColor,
            searchViewColor = searchViewColor,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onMenuBounds = { menuIconBounds = it }
        )



        /*Column(modifier = modifier
            .fillMaxWidth()
            .background(appHeaderBackground().copy(alpha = 0.8f) )// or your blur effect
            .padding(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(actionBarSizeDp()),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Words", color = colorOutlinedTextBorder(), style = MaterialTheme.typography.headlineMedium)

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
//                    .height(33.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                    ) {

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        onClick = { isSearchVisible = !isSearchVisible }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_search),
//                        painter = rememberVectorPainter(image = Icons.Default.Search),
                            contentDescription = "Search button",
                            modifier = Modifier
                                .size(width = 64.dp, height = 32.dp)
                                .background(color = appHeaderBackground().copy(alpha = 0.8f))
                                .border(
                                    width = 1.dp,
                                    color = searchColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp),
                            colorFilter = ColorFilter.tint(searchColor)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        onClick = { isMenuOpen = !isMenuOpen }
                    ) {
                        Box {
                            Image(
                                painter = painterResource(R.drawable.ic_menu),
//                            painter = rememberVectorPainter(image = Icons.Default.Menu),
                                contentDescription = "Menu button",
                                modifier = Modifier
                                    .size(width = 64.dp, height = 32.dp)
                                    .background(color = appHeaderBackground().copy(alpha = 0.8f))
                                    .border(
                                        width = 1.dp,
                                        color = menuColor,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .onGloballyPositioned {
                                        menuIconBounds = it.boundsInWindow().toIntRect()
                                    }
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(menuColor)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isSearchVisible,
                enter = animationPairBouncyScale.first,
                exit = animationPairBouncyScale.second
            )
            {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = {
                        Text(
                            "Search",
                            color = searchViewColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear text"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = searchViewColor,
                        unfocusedBorderColor = searchViewColor,
                        cursorColor = Color.Red,
                        focusedContainerColor = appHeaderBackground().copy(alpha = 0.85f),
                        unfocusedContainerColor = appHeaderBackground().copy(alpha = 0.5f),
                    )
                )
            }
        }*/
    }
/*
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp))
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Words", color = colorOutlinedTextBorder(), style = MaterialTheme.typography.headlineMedium)

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(33.dp), horizontalArrangement = Arrangement.End) {

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { isSearchVisible = !isSearchVisible }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_search),
//                        painter = rememberVectorPainter(image = Icons.Default.Search),
                        contentDescription = "Search button",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(color = Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = searchColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(searchColor)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { isMenuOpen = !isMenuOpen }
                ) {
                    Box {
                        Image(
                        painter = painterResource(R.drawable.ic_menu),
//                            painter = rememberVectorPainter(image = Icons.Default.Menu),
                            contentDescription = "Menu button",
                            modifier = Modifier
                                .size(width = 64.dp, height = 32.dp)
                                .background(color = Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    color = menuColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .onGloballyPositioned {
                                    menuIconBounds = it.boundsInWindow().toIntRect()
                                }
                                .padding(4.dp),
                            colorFilter = ColorFilter.tint(menuColor)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isSearchVisible,
            enter = animationPairBouncyScale.first,
            exit = animationPairBouncyScale.second
        )
         {
            OutlinedTextField(
                value = searchQuery,
                 onValueChange = { searchQuery = it },
                 label = {
                     Text(
                         "Search",
                         color = searchViewColor
                     )
                 },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(bottom = 8.dp),
                 singleLine = true,
                 trailingIcon = {
                     if (searchQuery.isNotEmpty()) {
                         IconButton(onClick = { searchQuery = "" }) {
                             Icon(
                                 imageVector = Icons.Default.Clear,
                                 contentDescription = "Clear text"
                             )
                         }
                     }
                 },
                 shape = RoundedCornerShape(13.dp),
                 colors = OutlinedTextFieldDefaults.colors(
                     focusedBorderColor = searchViewColor,
                     unfocusedBorderColor = searchViewColor,
                     cursorColor = searchViewColor,
                 )
            )
        }



        LazyColumn(state = listState) {
            item {
                Spacer(Modifier.height(0.dp)) // match your bottom bar height
            }
            items(filteredWords, key = { it.id }) { word ->
                val isExpanded = viewModel?.uiExpandCollapseState?.value?.contains(word.id) == true
                val isSelected = selectedWords.any { it.id == word.id }

                WordItemRow(
                    word = word,
                    isExpanded = isExpanded,
                    onAction =  { action -> action(action) },
                    enableSwipeStartToEnd = !isSelectionActive, // 🔹 disable swipes
                    enableSwipeEndToStart = !isSelectionActive,  // 🔹 disable swipes
                    isSelected = isSelected
                )
            }
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }
        }
    }
*/

    // 🔹 Popup menu
    HVDropdownMenu(
        anchorBounds = menuIconBounds,
        isOpen = isMenuOpen,
        onDismissRequest = { isMenuOpen = true },
        items = dropDownMenuItems,
        onItemClick = { menuItem ->
            isMenuOpen = false
            onMenuNavigate(menuItem.route)
        }
    )

    // Floating / expanding bottom bar
    FloatingOptionMenu(
        items = optionMenu,
        isOpen = isSelectionActive,
        isCollapsedOnScroll = isFloatingOptionMenuCollapse,
        onDismissRequest = { isMenuOpen = true },
        onItemClick = {}
    )


    Log.e("TAG009", "WordScreenContent: isFloatingOptionMenuCollapse= $isFloatingOptionMenuCollapse", )

}
@Composable
fun WordsHeader(
    modifier: Modifier = Modifier,
    selectedCount: Int,
    isSearchVisible: Boolean,
    onSearchToggle: () -> Unit,
    onMenuToggle: () -> Unit,
    onCancelSelection: () -> Unit,
    onSelectAll: () -> Unit,
    searchColor: Color,
    menuColor: Color,
    searchViewColor: Color,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMenuBounds: (IntRect) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(appHeaderBackground().copy(alpha = 0.8f) )// or your blur effect
            .padding(top = 0.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionBarSizeDp())
        ) {

            // --- Default header ---
            androidx.compose.animation.AnimatedVisibility(
                visible = selectedCount == 0,
                enter = enterSlideDown,
                exit = exitSlideUp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Words",
                        color = colorOutlinedTextBorder(),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Search
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            onClick = onSearchToggle
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = "Search button",
                                modifier = Modifier
                                    .size(width = 64.dp, height = 32.dp)
                                    .background(appHeaderBackground().copy(alpha = 0.8f))
                                    .border(1.dp, searchColor, RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(searchColor)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        // Menu
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            onClick = onMenuToggle
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_menu),
                                contentDescription = "Menu button",
                                modifier = Modifier
                                    .size(width = 64.dp, height = 32.dp)
                                    .background(appHeaderBackground().copy(alpha = 0.8f))
                                    .border(1.dp, menuColor, RoundedCornerShape(8.dp))
                                    .onGloballyPositioned { coords ->
                                        onMenuBounds(
                                            coords.boundsInWindow().toIntRect()
                                        )
                                    }
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(menuColor)
                            )
                        }
                    }
                }
            }

            // --- Selection header ---
            SelectionHeader(
                selectedCount = selectedCount,
                onSelectAll =  onSelectAll ,
                onCancelSelection = onCancelSelection
            )

           /* // --- Selection header ---
            androidx.compose.animation.AnimatedVisibility(
                visible = selectedCount > 0,
                enter = enterSlideDown,
                exit = exitSlideUp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$selectedCount selected", color = Color.Yellow, style = MaterialTheme.typography.headlineMedium)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Select All
                        Surface(shape = RoundedCornerShape(8.dp), onClick = onSelectAll) {
                            Image(
                                painter = painterResource(R.drawable.ic_select_all),
                                contentDescription = "Select All",
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(appHeaderBackground().copy(alpha = 0.8f))
                                    .border(1.dp, Color.Yellow, RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(Color.Yellow)
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        // Cancel selection
                        Surface(shape = RoundedCornerShape(8.dp), onClick = onCancelSelection) {
                            Image(
                                painter = painterResource(R.drawable.ic_cancel),
                                contentDescription = "Cancel Selection",
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(appHeaderBackground().copy(alpha = 0.8f))
                                    .border(1.dp, Color.Red, RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                colorFilter = ColorFilter.tint(Color.Red)
                            )
                        }
                    }
                }
            }*/

        }

        // SEARCH BAR BELOW HEADER
        AnimatedVisibility(
            visible = isSearchVisible,
            enter = animationPairBouncyScale.first,
            exit = animationPairBouncyScale.second
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Search", color = searchViewColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-8).dp)
                    .padding(bottom = 4.dp),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear text")
                        }
                    }
                },
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = searchViewColor,
                    unfocusedBorderColor = searchViewColor,
                    cursorColor = Color.Red,
                    focusedContainerColor = appHeaderBackground().copy(alpha = 0.85f),
                    unfocusedContainerColor = appHeaderBackground().copy(alpha = 0.5f),
                    focusedTextColor = Color.Red.copy(alpha = 0.5f),
                    focusedTrailingIconColor = searchViewColor
                )
            )
        }
    }
}

@Composable
fun SelectionHeader(
    selectedCount: Int,
    onSelectAll: () -> Unit,
    onCancelSelection: () -> Unit
) {
    // Define enter/exit animations
    val enterSlideDown = animationPairSlideUpDown.first
    val exitSlideUp = animationPairSlideUpDown.second

    androidx.compose.animation.AnimatedVisibility(
        visible = selectedCount > 0,
        enter = enterSlideDown,
        exit = exitSlideUp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$selectedCount selected",
                color = colorOutlinedTextBorder(),
                style = MaterialTheme.typography.headlineSmall
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Select All
                Surface(shape = RoundedCornerShape(8.dp), onClick = onSelectAll) {
                    Image(
                        painter = painterResource(R.drawable.ic_select_all),
                        contentDescription = "Select All",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(appHeaderBackground().copy(alpha = 0.8f))
                            .border(1.dp, colorOutlinedTextBorderDeActive(), RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(colorOutlinedTextBorderDeActive())
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Cancel selection
                Surface(shape = RoundedCornerShape(8.dp), onClick = onCancelSelection) {
                    Image(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = "Cancel Selection",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(appHeaderBackground().copy(alpha = 0.8f))
                            .border(1.dp, colorOutlinedTextBorderDeActive(), RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(colorOutlinedTextBorderDeActive())
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WordScreenPreview() {
    val sampleWords = listOf(
        Word(1, "Hello", "A greeting", "Hola", "ho-la", isFavorite = true),
        Word(2, "Book", "A collection of pages", "Libro", "lee-bro", false)
    )
    WordScreenContent(words = sampleWords)
}