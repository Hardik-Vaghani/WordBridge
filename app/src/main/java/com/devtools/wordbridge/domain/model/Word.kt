package com.devtools.wordbridge.domain.model

// domain/model/Word.kt
data class Word(
    val id: Int = 0,
    val primaryWord: String,
    val wordMeaning: String,
    val secondaryWord: String,
    val secondaryWordPronunciation: String,
    val isFavorite: Boolean
)
