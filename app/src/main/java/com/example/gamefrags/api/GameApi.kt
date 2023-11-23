package com.example.gamefrags.api

import com.example.gamefrags.model.Game
import retrofit2.Response
import retrofit2.http.GET

interface GameApi {

    @GET("games")
    suspend fun getAllGames():Response<Game>

}