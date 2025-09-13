package com.devtools.wordbridge.data.local

// data/local/WordEntity.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val primaryWord: String,
    val wordMeaning: String,
    val secondaryWord: String,
    val secondaryWordPronunciation: String
)