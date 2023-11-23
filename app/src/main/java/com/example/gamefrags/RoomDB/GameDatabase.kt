package com.example.gamefrags.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamefrags.model.GameItem

@Database(
    entities =[GameItem::class],
    version = 1
)
abstract class GameDatabase: RoomDatabase() {

    abstract fun getGameDao():GameDao

    companion object {
        @Volatile
        private var instance: GameDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it}
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GameDatabase::class.java,
                "game_db.db"
            ).build()
    }
}