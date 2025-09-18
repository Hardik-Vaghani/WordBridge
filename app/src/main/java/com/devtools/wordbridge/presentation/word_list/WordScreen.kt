package com.devtools.wordbridge.presentation.word_list

// presentation/wordlist/WordScreen.kt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import com.devtools.wordbridge.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.ui.CustomDropdownMenu
import com.devtools.wordbridge.presentation.ui.myItemsList
import com.devtools.wordbridge.presentation.ui.navigation.RememberBottomBarScrollState
import com.devtools.wordbridge.presentation.ui.theme.ColorError
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderSelectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorIconBorderUnselectedItem
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderActive
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorderDeActive
import com.devtools.wordbridge.presentation.ui.theme.enterBouncyHorizontal
import com.devtools.wordbridge.presentation.ui.theme.enter_03
import com.devtools.wordbridge.presentation.ui.theme.enter_05
import com.devtools.wordbridge.presentation.ui.theme.exitBouncy
import com.devtools.wordbridge.presentation.ui.theme.exit_03
import com.devtools.wordbridge.presentation.ui.theme.exit_04
import com.devtools.wordbridge.presentation.ui.theme.exit_05
import com.devtools.wordbridge.presentation.ui.theme.exit_07
import com.devtools.wordbridge.presentation.ui.toIntRect

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    viewModel: WordViewModel = hiltViewModel(),
    onBottomBarVisibilityChange: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {}
) {
    val words = viewModel.words.collectAsState()
    WordScreenContent(
        words = words.value,
        modifier = modifier,
        onBottomBarVisibilityChange = onBottomBarVisibilityChange,
        onMenuNavigate = onMenuNavigate
    )
}

@Composable
fun WordScreenContent(
    words: List<Word>,
    modifier: Modifier = Modifier,
    onBottomBarVisibilityChange: (Boolean) -> Unit = {},
    onMenuNavigate: (String) -> Unit = {}
) {

    val listState = rememberLazyListState()
    RememberBottomBarScrollState(listState, autoHideDelay = 2000L) { visible -> onBottomBarVisibilityChange(visible) }

    // 🔹 Search query state
    var searchQuery by remember { mutableStateOf("") }
    val searchViewColor by remember(searchQuery) { derivedStateOf { if (searchQuery.isBlank()) ColorOutlinedTextBorderDeActive else ColorOutlinedTextBorderActive.copy(alpha = 0.5f) } }
    var isSearchVisible by remember { mutableStateOf(false) }
    val searchColor by remember(isSearchVisible) { derivedStateOf { if (isSearchVisible) ColorIconBorderSelectedItem.copy(alpha = 0.5f) else ColorIconBorderUnselectedItem } }
    val density = LocalDensity.current
    // holds the icon's bounds in window coordinates
    var anchorBounds by remember { mutableStateOf(androidx.compose.ui.geometry.Rect.Zero) }


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
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text("Words", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)

            Row(modifier = Modifier.fillMaxWidth().height(33.dp), horizontalArrangement = Arrangement.End) {

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { isSearchVisible = !isSearchVisible }
                ) {
                    Image(
//                        painter = painterResource(R.drawable.ic_option),
                        painter = rememberVectorPainter(image = Icons.Default.Search),
                        contentDescription = "Search button",
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .background(color = Color.Transparent)
                            .border(width = 1.dp, color = searchColor, shape = RoundedCornerShape(8.dp))
                            .padding(4.dp)
                            // capture position + size of icon
                            .onGloballyPositioned { coordinates ->
                            anchorBounds = coordinates.boundsInWindow()
                        },
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
//                        painter = painterResource(R.drawable.ic_option),
                            painter = rememberVectorPainter(image = Icons.Default.Menu),
                            contentDescription = "Menu button",
                            modifier = Modifier
                                .size(width = 64.dp, height = 32.dp)
                                .background(color = Color.Transparent)
                                .border(width = 1.dp, color = menuColor, shape = RoundedCornerShape(8.dp))
                                .onGloballyPositioned { menuIconBounds = it.boundsInWindow().toIntRect() }
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

        LazyColumn(state = listState) {
            items(filteredWords) { word ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray) // your background color
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text("Primary: ${word.primaryWord}")
                        Text("Meaning: ${word.wordMeaning}")
                        Text("Secondary: ${word.secondaryWord}")
                        Text("Pronunciation: ${word.secondaryWordPronunciation}")
                    }
                }
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
        Word(1, "Hello", "A greeting", "Hola", "ho-la"),
        Word(2, "Book", "A collection of pages", "Libro", "lee-bro")
    )
    WordScreenContent(words = sampleWords)
}

