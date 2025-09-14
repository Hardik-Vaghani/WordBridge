package com.devtools.wordbridge.presentation.word_add

data class AddWordUiState(
    val word: String = "",
    val meaning: String = "",
    val translation: String = "",
    val pronunciation: String = ""
)