package com.example.gamefrags.ViewModelProviders

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.ui.dashboard.DashboardViewModel

class ViewModelProviderFactory(
    val app:Application,
    val gameRepo:GameRepo
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(app,gameRepo) as T
    }
}