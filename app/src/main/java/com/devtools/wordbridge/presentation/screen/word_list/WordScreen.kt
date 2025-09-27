package com.devtools.wordbridge.presentation.screen.word_list

// presentation/wordlist/WordScreen.kt
import android.util.Log
import com.devtools.wordbridge.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.action.UiActions
import com.devtools.wordbridge.presentation.ui.custom_ui.ActionItem
import com.devtools.wordbridge.presentation.ui.custom_ui.BottomActionBar
import com.devtools.wordbridge.presentation.ui.custom_ui.FloatingOptionMenu
import com.devtools.wordbridge.presentation.ui.custom_ui.HVDropdownMenu
import com.devtools.wordbridge.presentation.ui.custom_ui.WordItemRow
import com.devtools.wordbridge.presentation.ui.custom_ui.menuItems
import com.devtools.wordbridge.presentation.ui.navigation.RememberBottomBarScrollState
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderSelectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderActive
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderDeActive
import com.devtools.wordbridge.presentation.ui.custom_ui.toIntRect
import com.devtools.wordbridge.presentation.ui.theme.animationPairBouncyScale

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    onScrollVisibilityChangeOnOption: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {},
    navController: NavController? = null,
    viewModel: WordViewModel
) {
    val words by viewModel.words.collectAsState()
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
        modifier = modifier,
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
    RememberBottomBarScrollState(listState, autoHideDelay = 2000L) { visible -> onScrollVisibilityChangeOnOption(visible) }

    // 🔹 Search query state
    var searchQuery by remember { mutableStateOf("") }
    val searchViewColor by remember(searchQuery) { derivedStateOf { if (searchQuery.isBlank()) ColorOutlinedTextBorderDeActive else ColorOutlinedTextBorderActive.copy(alpha = 0.5f) } }
    var isSearchVisible by remember { mutableStateOf(false) }
    val searchColor by remember(isSearchVisible) { derivedStateOf { if (isSearchVisible) ColorIconBorderSelectedItem.copy(alpha = 0.5f) else ColorIconBorderUnselectedItem } }

    var isMenuOpen by remember { mutableStateOf(false) }
    val menuColor by remember(isMenuOpen) { derivedStateOf { if (isMenuOpen) ColorIconBorderSelectedItem.copy(alpha = 0.5f) else ColorIconBorderUnselectedItem } }
    var menuIconBounds by remember { mutableStateOf<IntRect?>(null) }

//    val isBarExpanded by viewModel?.isActionBarExpanded?.collectAsState()
    val isBarExpanded = viewModel?.isActionBarExpanded?.collectAsState(initial = false)?.value ?: false

    val actionItems = listOf(
        ActionItem(Icons.Default.Edit, "Edit") {
//            viewModel.editSelected()
        },
        ActionItem(Icons.Default.Delete, "Delete") {
//            viewModel.deleteSelected()
        },
        ActionItem(Icons.Default.Info, "Details", route = "details")
    )

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

    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text("Words", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)

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

//                        // 🔹 Popup menu
//                        HVDropdownMenu(
//                            anchorBounds = menuIconBounds,
//                            isOpen = isMenuOpen,
//                            onDismissRequest = { isMenuOpen = true },
//                            items = menuItems,
//                            onItemClick = { menuItem ->
//                                isMenuOpen = false
//                                onMenuNavigate(menuItem.route)
//                            }
//                        )

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
                     .padding(vertical = 8.dp),
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

        var showBottomBar by remember { mutableStateOf(false) }

        LazyColumn(state = listState) {
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }
            items(filteredWords, key = { it.id }) { word ->
                val isExpanded = viewModel?.uiExpandCollapseState?.value?.contains(word.id) == true
                WordItemRow(
                    word = word,
                    isExpanded = isExpanded,
                    onAction =  { action ->
                        when (action) {
                            is UiActions.LongPress -> {
                                val word = action.payload as? Word
                                if (word != null) {
                                    Log.e("TAG009", "WordScreenContent: ", )
                                    showBottomBar = true
                                }
                            }
                            is UiActions.Click -> {
                                val word = action.payload as? Word
                                if (word != null) {
                                    Log.e("TAG009", "WordScreenContent: ", )
                                    showBottomBar = true
                                }
                            }
                        }
                        action(action)

                        showBottomBar = false
                    }
                )
            }
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }
        }

        // Floating / expanding bottom bar
        if (showBottomBar){
            BottomActionBar(
    //            isShown = showBottomBar,
                isExpanded = !isBarExpanded,
                onToggle = { viewModel?.toggleActionBar() },
                actions = actionItems,
                onActionClick = { action ->
                    action.onAction?.invoke()
                    action.route?.let { navController?.navigate(it) }
                },
                modifier = Modifier
                //.align(Alignment.BottomCenter as Alignment.Horizontal)
            )
        }
    }

    // 🔹 Popup menu
    HVDropdownMenu(
        anchorBounds = menuIconBounds,
        isOpen = isMenuOpen,
        onDismissRequest = { isMenuOpen = true },
        items = menuItems,
        onItemClick = { menuItem ->
            isMenuOpen = false
            onMenuNavigate(menuItem.route)
        }
    )

    FloatingOptionMenu(items = menuItems){}
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

