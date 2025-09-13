package com.devtools.wordbridge.domain.usecase

// domain/usecase/GetAllWordsUseCase.kt
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetAllWordsUseCase(private val repository: WordRepository) {
    operator fun invoke(): Flow<List<Word>> = repository.getAllWords()
}
