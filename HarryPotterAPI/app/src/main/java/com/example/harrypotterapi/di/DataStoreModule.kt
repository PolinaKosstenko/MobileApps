package com.example.harrypotterapi.di

import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.harrypotterapi.data.UserRepository
import com.example.harrypotterapi.data.UserRepositoryImpl
import com.example.harrypotterapi.data.UserSerializer
import com.example.harrypotterapi.model.User
import dagger.Binds

val Context.dataStore: DataStore<User> by dataStore(
    fileName = "user.json",
    serializer = UserSerializer,
)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    companion object {

        @Provides
        @Singleton
        fun provideUserDataStore(
            @ApplicationContext applicationContext: Context
        ): DataStore<User> {
            return applicationContext.dataStore
        }
    }
}
