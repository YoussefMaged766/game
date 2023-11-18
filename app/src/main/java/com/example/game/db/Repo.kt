package com.example.game.db

import javax.inject.Inject

class Repo @Inject constructor(
    private val gameDao: GameDao,

) {

    suspend fun addGame(game: GameEntity) = gameDao.addGame(game)

    suspend fun getAllGames() = gameDao.getAllGames()

    suspend fun deleteAllGames() = gameDao.deleteAllGames()
}