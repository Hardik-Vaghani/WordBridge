package com.devtools.wordbridge.data

// data/WordRepositoryImpl.kt
import com.devtools.wordbridge.data.local.WordDao
import com.devtools.wordbridge.data.local.WordEntity
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRepositoryImpl(
    private val dao: WordDao
) : WordRepository {

    override suspend fun insertWord(word: Word) {
        dao.insertWord(word.toEntity())
    }

    override suspend fun deleteWord(word: Word) {
        dao.deleteWord(word.toEntity())
    }

    override fun getAllWords(): Flow<List<Word>> {
        return dao.getAllWords().map { list -> list.map { it.toDomain() } }
    }
}

// Mapping extensions
fun WordEntity.toDomain() = Word(id, primaryWord, wordMeaning, secondaryWord, secondaryWordPronunciation)
fun Word.toEntity() = WordEntity(id, primaryWord, wordMeaning, secondaryWord, secondaryWordPronunciation)
