package com.devtools.wordbridge.domain.usecase

// domain/usecase/DeleteWordUseCase.kt
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository

class DeleteWordUseCase(private val repository: WordRepository) {
    suspend operator fun invoke(word: Word) = repository.deleteWord(word)
}
