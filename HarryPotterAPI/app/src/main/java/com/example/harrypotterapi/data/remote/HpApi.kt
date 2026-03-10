package com.example.harrypotterapi.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface HpApi {

    @GET("characters")
    suspend fun characters(): List<HpApiDto>
}
