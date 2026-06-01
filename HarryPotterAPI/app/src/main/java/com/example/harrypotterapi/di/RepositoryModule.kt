package com.example.harrypotterapi.di

import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.data.CharacterRepositoryImpl
import com.example.harrypotterapi.data.SpellsRepository
import com.example.harrypotterapi.data.SpellsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCharacterRepository(
        impl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    abstract fun bindSpellRepository(
        impl: SpellsRepositoryImpl
    ): SpellsRepository
}
