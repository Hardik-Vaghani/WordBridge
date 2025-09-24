package com.devtools.wordbridge.presentation.screen.word_add

data class AddWordUiState(
    val word: String = "",
    val meaning: String = "",
    val translation: String = "",
    val pronunciation: String = ""
)