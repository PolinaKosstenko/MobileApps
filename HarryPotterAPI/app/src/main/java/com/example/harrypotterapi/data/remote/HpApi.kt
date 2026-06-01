package com.example.harrypotterapi.data.remote

import retrofit2.http.GET

interface HpApi {

    @GET("characters")
    suspend fun characters(): List<HpApiDto>

    @GET("spells")
    suspend fun spells(): List<HpApiSpellDto>
}
