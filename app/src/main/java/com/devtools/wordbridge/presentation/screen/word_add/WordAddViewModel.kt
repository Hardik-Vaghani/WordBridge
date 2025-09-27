package com.devtools.wordbridge.presentation.screen.word_add

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.usecase.AddWordUseCase
import com.devtools.wordbridge.domain.usecase.GetSingleWordUseCase
import com.devtools.wordbridge.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first


@HiltViewModel
class WordAddViewModel @Inject constructor(
    private val addWordUseCase: AddWordUseCase,
    private val getSingleWordUseCase: GetSingleWordUseCase
) : BaseViewModel() {

    private val _id = MutableStateFlow(0)
    val id = _id.asStateFlow()

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

    fun getWordById(wordId: Int) {
        viewModelScope.launch {
            try {
                val word = getSingleWordUseCase(wordId).first()
                setWordId(word.id)
                onWordChanged(word.primaryWord)
                onMeaningChanged(word.wordMeaning)
                onTranslationChanged(word.secondaryWord)
                onPronunciationChanged(word.secondaryWordPronunciation)
                onToggleFavourite(word.isFavorite)
            } catch (e: Exception) {
                Log.e("WordAddViewModel", "Error loading word with id $wordId: ${e.message}")
            }
        }
    }

    fun setWordId(wordId: Int) { _id.value = wordId }
    fun onWordChanged(newWord: String) { _word.value = newWord }
    fun onMeaningChanged(newMeaning: String) { _meaning.value = newMeaning }
    fun onTranslationChanged(newTranslation: String) { _translation.value = newTranslation }
    fun onPronunciationChanged(newPronunciation: String) { _pronunciation.value = newPronunciation }

    fun onToggleFavourite(newFavorite: Boolean) { _isFavorite.value = newFavorite }

    fun saveWord(status: (OperationStatus<Word>?) -> Unit = {}) {
        viewModelScope.launch {
            // Insert word
            val newWord = Word(
                primaryWord = _word.value,
                wordMeaning = _meaning.value,
                secondaryWord = _translation.value,
                secondaryWordPronunciation = _pronunciation.value,
                isFavorite = _isFavorite.value
            )
            upsertWord(word = newWord, status = status)
        }
    }

    fun updateWord(status: (OperationStatus<Word>?) -> Unit = {}) {
        viewModelScope.launch {
            val updatedWord = Word(
                id = _id.value,
                primaryWord = _word.value,
                wordMeaning = _meaning.value,
                secondaryWord = _translation.value,
                secondaryWordPronunciation = _pronunciation.value,
                isFavorite = _isFavorite.value
            )
            upsertWord(word = updatedWord, status = status)
        }
    }

    private fun clearInputs() {
        setWordId(0)
        onWordChanged("")
        onMeaningChanged("")
        onTranslationChanged("")
        onPronunciationChanged("")
        onToggleFavourite(false)
    }

    override fun upsertWord(word: Word, status: (OperationStatus<Word>?) -> Unit) {
        viewModelScope.launch {
            val result = addWordUseCase(word)
            status(result) // propagate back to caller
            clearInputs()
        }
    }
}
