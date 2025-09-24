package com.devtools.wordbridge.presentation.screen.word_list

// presentation/wordlist/WordViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtools.wordbridge.domain.action.WordAction
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.usecase.AddWordUseCase
import com.devtools.wordbridge.domain.usecase.DeleteWordUseCase
import com.devtools.wordbridge.domain.usecase.GetAllWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val addWordUseCase: AddWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    getAllWordsUseCase: GetAllWordsUseCase
) : ViewModel() {

    val words = getAllWordsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onAction(action: WordAction) {
        when (action) {
//            is WordAction.Edit -> { }
            is WordAction.Delete -> deleteWord(action.word)
            is WordAction.ToggleFavourite -> onToggleFavourite(action.wordId, action.isFavorite)
            is WordAction.ExpandCollapse -> { expandCollapse(action.wordId) }
            else -> { /* ignore other actions */ }
        }
    }
    private fun addWord(word: Word) {
        viewModelScope.launch {
            addWordUseCase(word)
        }
    }

    private fun deleteWord(word: Word) {
        viewModelScope.launch {
            deleteWordUseCase(word)
        }
    }

    private fun onToggleFavourite(id: Int, isStarred: Boolean) {
        viewModelScope.launch {
            val updatedWord = words.value.find { it.id == id }?.copy(isFavorite = isStarred)
            if (updatedWord != null) {
                addWord(updatedWord)
            }
        }
    }
    // State holding the set of expanded word IDs
    private val _uiExpandCollapseState = androidx.compose.runtime.mutableStateOf<Set<Int>>(emptySet())
    val uiExpandCollapseState: androidx.compose.runtime.State<Set<Int>> = _uiExpandCollapseState

    // Toggle expand/collapse for a word
    fun expandCollapse(wordId: Int) {
        _uiExpandCollapseState.value =
            if (_uiExpandCollapseState.value.contains(wordId))
                _uiExpandCollapseState.value - wordId
            else
                _uiExpandCollapseState.value + wordId
    }
}
