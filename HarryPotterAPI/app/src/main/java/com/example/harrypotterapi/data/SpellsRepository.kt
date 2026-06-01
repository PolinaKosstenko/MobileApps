package com.example.harrypotterapi.data

import com.example.harrypotterapi.data.local.SpellDao
import com.example.harrypotterapi.data.local.toDomain
import com.example.harrypotterapi.data.remote.HpApi
import com.example.harrypotterapi.data.remote.dtoToCharacter
import com.example.harrypotterapi.data.remote.dtoToSpell
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.Spell
import com.example.harrypotterapi.model.toEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface SpellsRepository {
    fun getSpells(): Flow<List<Spell>>
    fun getSpell(id: Int): Flow<Spell?>
    suspend fun isEmpty(): Boolean
}

class SpellsRepositoryImpl @Inject constructor(
    private val api: HpApi,
    private val spellsDao: SpellDao
) : SpellsRepository {

    override fun getSpells(): Flow<List<Spell>> =
        spellsDao.getAllFlow()
            .map { list -> list.map { it.toDomain() } }
            .onStart {
                val current = spellsDao.getAllFlow().first()
                if (current.isEmpty()) {
                    spellsDao.addSpells(
                        api.spells().mapIndexed { i, v ->
                            v.dtoToSpell(i).toEntity()
                        }
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    override fun getSpell(id: Int): Flow<Spell?> =
        spellsDao.getSpell(id)
            .map { it?.toDomain() }
            .flowOn(Dispatchers.IO)

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) {
            spellsDao.getAllFlow().first().isEmpty()
        }
}
