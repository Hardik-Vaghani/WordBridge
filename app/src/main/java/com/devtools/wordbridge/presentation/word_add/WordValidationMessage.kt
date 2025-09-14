package com.devtools.wordbridge.presentation.word_add

sealed class WordValidationMessage {
    data class Error(val message: String) : WordValidationMessage()
    data class Warning(val message: String) : WordValidationMessage()
    data class Success(val message: String) : WordValidationMessage()
    data class Information(val message: String) : WordValidationMessage()
}
