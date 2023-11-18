package com.example.game.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun addGame(game: GameEntity)

    @Query("SELECT * FROM   game  ")
     fun getAllGames(): Flow<List<GameEntity>>

     @Query("DELETE FROM game")
        suspend fun deleteAllGames()

}