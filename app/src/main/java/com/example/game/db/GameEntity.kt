package com.example.game.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val img1: Int,
    val img2: Int,
    val img3: Int,
    val currentAssets: Int,
    val currentBet: Int,
    val gain: Int? = null,
    val lose: Int? = null,
    val isWin: Boolean
)
