package com.example.gamefrags.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gamefrags.model.GameItem

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(game:GameItem):Long

    @Query("SELECT * FROM games")
    fun getAllGames(): LiveData<List<GameItem>>

    @Delete
    suspend fun deleteGame(game: GameItem)
}