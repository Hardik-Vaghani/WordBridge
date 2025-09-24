package com.devtools.wordbridge.app.di

// di/AppModule.kt
import android.content.Context
import androidx.room.Room
import com.devtools.wordbridge.data.WordRepositoryImpl
import com.devtools.wordbridge.data.local.MIGRATION_1_2
import com.devtools.wordbridge.data.local.WordDao
import com.devtools.wordbridge.data.local.WordDatabase
import com.devtools.wordbridge.domain.repository.WordRepository
import com.devtools.wordbridge.domain.usecase.AddWordUseCase
import com.devtools.wordbridge.domain.usecase.DeleteWordUseCase
import com.devtools.wordbridge.domain.usecase.GetAllWordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): WordDatabase =
        Room.databaseBuilder(app, WordDatabase::class.java, "word_db")
            .addMigrations(MIGRATION_1_2) // add your migrations here too!
            .build()

    @Provides
    fun provideWordDao(db: WordDatabase): WordDao = db.wordDao()

    @Provides
    @Singleton
    fun provideWordRepository(dao: WordDao): WordRepository = WordRepositoryImpl(dao)

    // Provide UseCases
    @Provides
    fun provideAddWordUseCase(repository: WordRepository) = AddWordUseCase(repository)

    @Provides
    fun provideDeleteWordUseCase(repository: WordRepository) = DeleteWordUseCase(repository)

    @Provides
    fun provideGetAllWordsUseCase(repository: WordRepository) = GetAllWordsUseCase(repository)
}
