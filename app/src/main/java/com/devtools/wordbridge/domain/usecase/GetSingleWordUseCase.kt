package com.devtools.wordbridge.domain.usecase

// domain/usecase/GetAllWordsUseCase.kt
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetSingleWordUseCase(private val repository: WordRepository) {
    operator fun invoke(id: Int): Flow<Word> = repository.getWord(id = id)
}
