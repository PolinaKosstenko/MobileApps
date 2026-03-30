package com.example.harrypotterapi.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.harrypotterapi.model.House
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class FavouriteCharacterDaoTest {
    private  lateinit var database: CharacterDatabase
    private  lateinit var dao: FavouriteCharacterDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CharacterDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.favouriteCharacterDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insert_AngGetAll_returnsItemsOrderedByAddedAtDesc() = runTest {
        val oldItem = FavouriteCharacterEntity(
            1,
            "Harry Potter",
            "male",
            LocalDate.of(1980, 7, 31),
            "green",
            "black",
            "human",
            true,
            House.Gryffindor,
            "holly",
            "phoenix tail feather",
            "stag",
        )

        val newItem = FavouriteCharacterEntity(
            2,
            "Severus Snape",
            "male",
            LocalDate.of(1960, 1, 9),
            "black",
            "black",
            "human",
            true,
            House.Slytherin,
            "No wand wood",
            "No wand core",
            "doe",
        )

        dao.insert(oldItem)
        dao.insert(newItem)
        val result = dao.getAll()

        assertEquals(2, result.size)

        assertTrue(result.any { it.id == 1 })
        assertTrue(result.any { it.id == 2 })
    }

}