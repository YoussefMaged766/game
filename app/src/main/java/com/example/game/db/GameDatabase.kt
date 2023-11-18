package com.example.game.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class], version = 2)
abstract class GameDatabase :RoomDatabase() {
    abstract fun gameDao(): GameDao
}