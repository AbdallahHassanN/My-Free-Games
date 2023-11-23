package com.example.gamefrags.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.model.GameItem
import kotlinx.coroutines.launch

class HomeViewModel( private val gameRepo: GameRepo) : ViewModel() {

    fun saveGame(game: GameItem) = viewModelScope.launch {
        gameRepo.upsert(game)
    }

    fun getSavedGames() = gameRepo.getSavedGames()

    fun deleteGame(game: GameItem) = viewModelScope.launch {
        gameRepo.deleteGame(game)
    }
}