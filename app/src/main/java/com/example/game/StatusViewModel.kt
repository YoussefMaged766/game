package com.example.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.db.GameEntity
import com.example.game.db.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val _stateProduct = MutableStateFlow<List<GameEntity>>(emptyList())
    val stateProduct = _stateProduct.asStateFlow()

    private val currentAssetsFlow = MutableStateFlow(100)

    private val currentImagesFlow = MutableStateFlow(listOf(R.drawable.img1, R.drawable.img2, R.drawable.img3))

    fun getCurrentImagesFlow(): StateFlow<List<Int>> = currentImagesFlow

    fun setCurrentImagesFlow(newImages: List<Int>) {
        currentImagesFlow.value = newImages
    }

    fun getCurrentAssetsFlow(): StateFlow<Int> = currentAssetsFlow

    fun setCurrentAssetsFlow(newAssets: Int) {
        currentAssetsFlow.value = newAssets
    }

    fun decreaseFromAssets(currentBet: Int) {
        val currentAssets = currentAssetsFlow.value
        if (currentAssets - currentBet >= 0) {
            currentAssetsFlow.value = currentAssets - currentBet
        }
    }

    init {
        getAllGames()
    }

     fun getAllGames() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllGames().collect {
            _stateProduct.emit(it)
        }
    }

    suspend fun insertGame(game: GameEntity) = repo.addGame(game)

     fun deleteAllGames() = viewModelScope.launch {
         repo.deleteAllGames()
     }
}