package com.devtools.wordbridge.domain.usecase

import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavoriteWordsUseCase(private val repository: WordRepository) {
    operator fun invoke(): Flow<List<Word>> = repository.getAllFavoriteWords()
}
