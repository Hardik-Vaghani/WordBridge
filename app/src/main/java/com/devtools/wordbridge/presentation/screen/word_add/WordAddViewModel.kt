package com.devtools.wordbridge.presentation.screen.word_add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.usecase.AddWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class WordAddViewModel @Inject constructor(
    private val addWordUseCase: AddWordUseCase
) : ViewModel() {

    private val _word = MutableStateFlow("")
    val word = _word.asStateFlow()

    private val _meaning = MutableStateFlow("")
    val meaning = _meaning.asStateFlow()

    private val _translation = MutableStateFlow("")
    val translation = _translation.asStateFlow()

    private val _pronunciation = MutableStateFlow("")
    val pronunciation = _pronunciation.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun onWordChanged(newWord: String) { _word.value = newWord }
    fun onMeaningChanged(newMeaning: String) { _meaning.value = newMeaning }
    fun onTranslationChanged(newTranslation: String) { _translation.value = newTranslation }
    fun onPronunciationChanged(newPronunciation: String) { _pronunciation.value = newPronunciation }

    fun onToggleFavourite(newFavorite: Boolean) { _isFavorite.value = newFavorite }

    fun saveWord(onSaved: (OperationStatus<Word>?) -> Unit = {}) {
        viewModelScope.launch {
            // Insert word
            val newWord = Word(
                primaryWord = _word.value,
                wordMeaning = _meaning.value,
                secondaryWord = _translation.value,
                secondaryWordPronunciation = _pronunciation.value,
                isFavorite = _isFavorite.value
            )

            val result: OperationStatus<Word> = addWordUseCase(newWord)

            // Clear inputs
            clearInputs()

            // Trigger onSaved callback
            onSaved(result)
        }
    }

    private fun clearInputs() {
        _word.value = ""
        _meaning.value = ""
        _translation.value = ""
        _pronunciation.value = ""
        _isFavorite.value = false
    }
}
