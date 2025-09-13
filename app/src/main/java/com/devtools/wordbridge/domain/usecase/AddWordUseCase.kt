package com.devtools.wordbridge.domain.usecase

// domain/usecase/AddWordUseCase.kt
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository

class AddWordUseCase(private val repository: WordRepository) {
    suspend operator fun invoke(word: Word) = repository.insertWord(word)
}
