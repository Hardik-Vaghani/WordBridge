package com.devtools.wordbridge.presentation.screen.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devtools.wordbridge.R
import com.devtools.wordbridge.core.action.Action
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.screen.word_list.WordViewModel
import com.devtools.wordbridge.presentation.ui.custom_ui.WordItemRow
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderSelectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderActive
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderDeActive
import com.devtools.wordbridge.presentation.ui.theme.animationPairBouncyScale

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    navController: NavController? = null,
    viewModel: WordViewModel
) {

    val words = viewModel.favoriteWords.collectAsState()
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

    FavoriteScreenContent(
        words = words.value,
        modifier = modifier,
        wordAction = { action -> viewModel.onAction(action) },
        viewModel = viewModel,
        onBackClicked = onBackClicked
    )
}

@Composable
fun FavoriteScreenContent(
    modifier: Modifier = Modifier,
    words: List<Word>,
    viewModel: WordViewModel? = null,
    wordAction: (Action) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    // 🔹 Search query state
    var searchQuery by remember { mutableStateOf("") }
    val searchViewColor by remember(searchQuery) { derivedStateOf { if (searchQuery.isBlank()) ColorOutlinedTextBorderDeActive else ColorOutlinedTextBorderActive.copy(alpha = 0.5f) } }
    var isSearchVisible by remember { mutableStateOf(false) }
    val searchColor by remember(isSearchVisible) { derivedStateOf { if (isSearchVisible) ColorIconBorderSelectedItem.copy(alpha = 0.5f) else ColorIconBorderUnselectedItem } }

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

            Text("Favorite", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)

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
                    onClick = { onBackClicked() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        //                        painter = rememberVectorPainter(image = Icons.Default.Search),
                        contentDescription = "Back button",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(color = Color.Transparent)
                            .border(width = 1.dp, color = ColorIconBorderUnselectedItem, shape = RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(ColorIconBorderUnselectedItem)
                    )
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

        //val expandedState = remember { mutableStateMapOf<Int, Boolean>() }

        LazyColumn(state = listState) {
            item {
                Spacer(Modifier.height(16.dp)) // match your bottom bar height
            }

            items(filteredWords, key = { it.id }) { word ->
                val isExpanded = viewModel?.uiExpandCollapseState?.value?.contains(word.id) == true

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
fun FavoriteScreenPreview() {
    val sampleWords = listOf(
        Word(1, "Hello", "A greeting", "Hola", "ho-la", isFavorite = true),
        Word(2, "Book", "A collection of pages", "Libro", "lee-bro", false)
    )
    FavoriteScreenContent(words = sampleWords)
}

