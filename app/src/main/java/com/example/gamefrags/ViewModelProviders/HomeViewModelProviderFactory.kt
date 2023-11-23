package com.example.gamefrags.ViewModelProviders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.ui.home.HomeViewModel

class HomeViewModelProviderFactory( val gameRepo: GameRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(gameRepo) as T
    }

}