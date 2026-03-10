package com.example.harrypotterapi.di

import android.content.Context
import androidx.room.Room
import com.example.harrypotterapi.data.local.CharacterDatabase
import com.example.harrypotterapi.data.local.FavouriteCharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CharacterDatabase =
        Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            "character.db"
        ).build()

    @Provides
    fun providesFavouriteDao(db: CharacterDatabase): FavouriteCharacterDao =
        db.favouriteCharacterDao()
}