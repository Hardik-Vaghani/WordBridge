package com.devtools.wordbridge.presentation.screen.word_list

// presentation/wordlist/WordViewModel.kt
import androidx.lifecycle.viewModelScope
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.usecase.AddWordUseCase
import com.devtools.wordbridge.domain.usecase.DeleteWordUseCase
import com.devtools.wordbridge.domain.usecase.GetAllFavoriteWordsUseCase
import com.devtools.wordbridge.domain.usecase.GetAllWordsUseCase
import com.devtools.wordbridge.presentation.BaseViewModel
import com.devtools.wordbridge.presentation.ui.navigation.NavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val addWordUseCase: AddWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    getAllWordsUseCase: GetAllWordsUseCase,
    getAllFavoriteWordsUseCase: GetAllFavoriteWordsUseCase,
) : BaseViewModel() {

//    val wordsFlow = getAllWordsUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val wordsFlow = super.words

    init {
        viewModelScope.launch {
            getAllWordsUseCase().collect { list ->
                setWordsList(list)
            }
        }
    }
    val favoriteWords = getAllFavoriteWordsUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()
//    private val _isActionBarExpanded = MutableStateFlow(false)
//    val isActionBarExpanded = _isActionBarExpanded.asStateFlow()
//    fun toggleActionBar() { _isActionBarExpanded.value = !_isActionBarExpanded.value }

    override fun navigateToUpdateScreen(wordId: Int) {
        // Create route with wordId as parameter
        val route = NavItem.WordUpdate.createRoute(wordId) //NavItem.WordUpdate.route.replace("{wordId}", wordId.toString())
        _navigationEvent.value = route
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    override fun upsertWord(word: Word, status: (OperationStatus<Word>?) -> Unit) {
        viewModelScope.launch { status(addWordUseCase(word)) }
    }

    override fun deleteWord(word: Word, status: (OperationStatus<Word>?) -> Unit) {
        viewModelScope.launch { status(deleteWordUseCase(word)) }
    }

    override fun onToggleFavourite(wordId: Int, isFav: Boolean) {
        viewModelScope.launch {
            val updatedWord = wordsFlow.value.find { it.id == wordId }?.copy(isFavorite = isFav)
            if (updatedWord != null) {
                upsertWord(updatedWord)
            }
        }
    }
    // State holding the set of expanded word IDs
    private val _uiExpandCollapseState = androidx.compose.runtime.mutableStateOf<Set<Int>>(emptySet())
    val uiExpandCollapseState: androidx.compose.runtime.State<Set<Int>> = _uiExpandCollapseState

    // Toggle expand/collapse for a word
    override fun expandCollapse(wordId: Int) {
        _uiExpandCollapseState.value =
            if (_uiExpandCollapseState.value.contains(wordId))
                _uiExpandCollapseState.value - wordId
            else
                _uiExpandCollapseState.value + wordId
    }
}
