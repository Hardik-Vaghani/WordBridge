package com.devtools.wordbridge.presentation.word_list

// presentation/wordlist/WordScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.presentation.ui.navigation.RememberBottomBarScrollState
import com.devtools.wordbridge.presentation.ui.theme.ColorOutlinedTextBorder

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    viewModel: WordViewModel = hiltViewModel(),
    onBottomBarVisibilityChange: (Boolean) -> Unit = {}
) {
    val words = viewModel.words.collectAsState()
    WordScreenContent(
        words = words.value,
        modifier = modifier,
        onBottomBarVisibilityChange = onBottomBarVisibilityChange
    )
}

@Composable
fun WordScreenContent(
    words: List<Word>,
    modifier: Modifier = Modifier,
    onBottomBarVisibilityChange: (Boolean) -> Unit = {}
) {

    val listState = rememberLazyListState()
    var lastScrollOffset by remember { mutableStateOf(0) }
    RememberBottomBarScrollState(listState, autoHideDelay = 2000L) { visible -> onBottomBarVisibilityChange(visible) }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
        Text("Words", color = ColorOutlinedTextBorder, style = MaterialTheme.typography.headlineMedium)

        LazyColumn(state = listState) {
            items(words) { word ->
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

