package com.example.harrypotterapi.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import com.example.harrypotterapi.model.Grade
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.User
import com.example.quiz.ui.theme.ThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

interface UserRepository {
    fun getCharacter(): Flow<Int>
    fun getGrades(): Flow<Map<String, Grade>>
    fun getSelectedTheme(): Flow<ThemeStyle>
    suspend fun setCharacter(id: Int)
    suspend fun setGrade(spell: String, grade: Grade)
    suspend fun resetGrades()
    suspend fun resetCharacter()
    fun getLocation(): Flow<House>
    suspend fun setLocation(house: House)
    suspend fun setSelectedTheme(theme: ThemeStyle)
}

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: DataStore<User>
) : UserRepository {

    override fun getCharacter(): Flow<Int> = userDataStore.data.map {
        user -> user.characterId
    }

    override fun getGrades(): Flow<Map<String, Grade>> = userDataStore.data.map {
        user -> user.grades
    }

    override fun getSelectedTheme(): Flow<ThemeStyle> = userDataStore.data.map {
        user -> user.selectedTheme
    }

    override suspend fun setCharacter(id: Int) {
        userDataStore.updateData { user ->
            user.copy(characterId = id)
        }
    }
    override suspend fun setGrade(spell: String, grade: Grade) {
        userDataStore.updateData { user ->
            user.copy(grades = user.grades + (spell to grade))
        }
    }

    override suspend fun resetGrades() {
        userDataStore.updateData { user ->
            user.copy(grades = mapOf())
        }
    }

    override suspend fun resetCharacter() {
        userDataStore.updateData { user ->
            user.copy(
                characterId = Int.MIN_VALUE,
                selectedTheme = ThemeStyle.NEUTRAL,
                grades = mapOf(),
                selectedLocation = House.NoHouse
            )
        }
    }

    override fun getLocation(): Flow<House> = userDataStore.data.map {
        user -> user.selectedLocation
    }

    override suspend fun setLocation(house: House) {
        userDataStore.updateData { user ->
            user.copy(selectedLocation = house)
        }
    }

    override suspend fun setSelectedTheme(theme: ThemeStyle) {
        userDataStore.updateData { user ->
            user.copy(selectedTheme = theme)
        }
    }
}

object UserSerializer : Serializer<User> {

    override val defaultValue: User = User(
        characterId = Int.MIN_VALUE,
        grades = mapOf(),
        selectedLocation = House.NoHouse,
        selectedTheme = ThemeStyle.NEUTRAL
    )

    override suspend fun readFrom(input: InputStream): User =
        try {
            Json.decodeFromString<User>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read User", serialization)
        }

    override suspend fun writeTo(t: User, output: OutputStream) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }
}
