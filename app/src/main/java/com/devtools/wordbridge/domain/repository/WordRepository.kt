package com.devtools.wordbridge.domain.repository

// domain/repository/WordRepository.kt
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    suspend fun insertWord(word: Word): OperationStatus<Word>
    suspend fun deleteWord(word: Word): OperationStatus<Word>
    fun getAllWords(): Flow<List<Word>>
}
