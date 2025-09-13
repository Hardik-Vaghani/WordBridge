package com.devtools.wordbridge.domain.repository

// domain/repository/WordRepository.kt
import com.devtools.wordbridge.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    suspend fun insertWord(word: Word)
    suspend fun deleteWord(word: Word)
    fun getAllWords(): Flow<List<Word>>
}
