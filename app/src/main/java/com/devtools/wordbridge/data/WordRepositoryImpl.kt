package com.devtools.wordbridge.data

// data/WordRepositoryImpl.kt
import com.devtools.wordbridge.data.local.WordDao
import com.devtools.wordbridge.data.local.WordEntity
import com.devtools.wordbridge.domain.common.OperationStatus
import com.devtools.wordbridge.domain.model.Word
import com.devtools.wordbridge.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRepositoryImpl(
    private val dao: WordDao
) : WordRepository {

    override suspend fun insertWord(word: Word): OperationStatus<Word> {
        return try {
            val rowId = dao.insertWord(word.toEntity())
            if (rowId != -1L) {
                OperationStatus.Success(data = word, message = "\"${word.primaryWord}\" word inserted successfully ✅")
            } else {
                OperationStatus.Failed(data = word, message = "Insertion failed")
            }
        } catch (e: Exception) {
            OperationStatus.Failed(data = word, message = "Error: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteWord(word: Word): OperationStatus<Word> {
        return try {
            val rowId = dao.deleteWord(word.toEntity())
            if (rowId != -1) {
            OperationStatus.Success(data = word, message = "\"${word.primaryWord}\" word deleted successfully ✅")}
            else {
                OperationStatus.Failed(data = word, message = "Deletion failed")
            }
        } catch (e: Exception) {
            OperationStatus.Failed(data = word, message = "Error: ${e.localizedMessage}")
        }
    }

    override fun getAllWords(): Flow<List<Word>> {
        return dao.getAllWords().map { list -> list.map { it.toDomain() } }
    }
}

// Mapping extensions
fun WordEntity.toDomain() = Word(id, primaryWord, wordMeaning, secondaryWord, secondaryWordPronunciation, isFavorite)
fun Word.toEntity() = WordEntity(id, primaryWord, wordMeaning, secondaryWord, secondaryWordPronunciation, isFavorite)
