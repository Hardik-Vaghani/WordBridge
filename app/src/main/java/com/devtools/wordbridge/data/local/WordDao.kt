package com.devtools.wordbridge.data.local

// data/local/WordDao.kt
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity): Long?

    @Delete
    suspend fun deleteWord(word: WordEntity): Int?

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>
}
