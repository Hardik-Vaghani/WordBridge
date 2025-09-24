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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devtools.wordbridge.domain.action.WordAction
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.ui.custom_ui.CustomDropdownMenu
import com.devtools.wordbridge.presentation.ui.custom_ui.WordItemRow
import com.devtools.wordbridge.presentation.ui.custom_ui.myItemsList
import com.devtools.wordbridge.presentation.ui.navigation.RememberBottomBarScrollState
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderSelectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderActive
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderDeActive
import com.devtools.wordbridge.presentation.ui.theme.enter_03
import com.devtools.wordbridge.presentation.ui.theme.exitBouncy
import com.devtools.wordbridge.presentation.ui.custom_ui.toIntRect

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    viewModel: WordViewModel = hiltViewModel(),
    onScrollVisibilityChangeOnOption: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {}
) {
    val words = viewModel.words.collectAsState()
    val expandedIds: Set<Int> by viewModel.uiExpandCollapseState
    WordScreenContent(
        words = words.value,
        modifier = modifier,
        onScrollVisibilityChangeOnOption = onScrollVisibilityChangeOnOption,
        onMenuNavigate = onMenuNavigate,
        wordAction = { action -> viewModel.onAction(action) },
        //onToggleFavourite = { id, isStarred -> viewModel.onAction(WordAction.ToggleFavourite(id, isStarred)) }
    )
}

@Composable
fun WordScreenContent(
    modifier: Modifier = Modifier,
    words: List<Word>,
    viewModel: WordViewModel = hiltViewModel(),
    onScrollVisibilityChangeOnOption: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {},
    wordAction: (WordAction) -> Unit = {},
    //onToggleFavourite: (Int, Boolean) -> Unit = { _, _ -> }
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

                        // 🔹 Popup menu
                        CustomDropdownMenu(
                            anchorBounds = menuIconBounds,
                            isOpen = isMenuOpen,
                            onDismissRequest = { isMenuOpen = true },
                            items = myItemsList,
                            onItemClick = { menuItem ->
                                isMenuOpen = false
                                onMenuNavigate(menuItem.route)
                            }
                        )

                    }
                }


            }
        }

        AnimatedVisibility(
            visible = isSearchVisible,
            enter = enter_03,
            exit = exitBouncy
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

        //val expandedState = remember { mutableStateMapOf<Int, Boolean>() }

        LazyColumn(state = listState) {
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }

            items(filteredWords, key = { it.id }) { word ->
                val expandedIds by viewModel.uiExpandCollapseState
                val isExpanded = expandedIds.contains(word.id)

                WordItemRow(
                    word = word,
                    isExpanded = isExpanded,
                    onAction =  wordAction
                )
            }
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
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

