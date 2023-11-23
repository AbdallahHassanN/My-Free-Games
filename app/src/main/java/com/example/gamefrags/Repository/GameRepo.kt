package com.example.gamefrags.Repository

import com.example.gamefrags.RoomDB.GameDatabase
import com.example.gamefrags.api.RetrofitBuilder
import com.example.gamefrags.model.GameItem

class GameRepo(
    val db: GameDatabase
) {

    suspend fun getAllGames() = RetrofitBuilder.api.getAllGames()

    suspend fun upsert(game:GameItem) = db.getGameDao().upsert(game)

    fun getSavedGames() = db.getGameDao().getAllGames()

    suspend fun deleteGame(game:GameItem) = db.getGameDao().deleteGame(game)

}