package com.devtools.wordbridge.presentation.word_list

// presentation/wordlist/WordViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun addWord(word: Word) {
        viewModelScope.launch {
            addWordUseCase(word)
        }
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch {
            deleteWordUseCase(word)
        }
    }
}
