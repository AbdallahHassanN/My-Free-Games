package com.example.gamefrags.ui.dashboard

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.example.gamefrags.GameApplication
import com.example.gamefrags.Repository.GameRepo
import com.example.gamefrags.Response.Resource
import com.example.gamefrags.model.Game
import com.example.gamefrags.model.GameItem
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DashboardViewModel(
    app:Application,
    private val gameRepo:GameRepo)
    : AndroidViewModel(app) {

    val allGames: MutableLiveData<Resource<Game>> = MutableLiveData()

    init {

        getAllGames()
    }
    private fun getAllGames() = viewModelScope.launch {
        /*allGames.postValue(Resource.Loading())
        val response = gameRepo.getAllGames()
        allGames.postValue(handleGamesResponse(response))*/
        safeHandleGamesCall()
    }

    private fun handleGamesResponse(response: Response<Game>): Resource<Game> {
        if(response.isSuccessful){
            response.body().let {
                return Resource.Success(it!!)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveGame(game:GameItem) = viewModelScope.launch {
        gameRepo.upsert(game)
    }



    private suspend fun safeHandleGamesCall(){
        allGames.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = gameRepo.getAllGames()
                allGames.postValue(handleGamesResponse(response))
            } else {
                allGames.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t:Throwable){
            when(t) {
                is IOException -> allGames.postValue(Resource.Error("Network Failure"))
                else -> allGames.postValue(Resource.Error("Conversion Failure"))
            }
        }
    }


    private fun hasInternetConnection():Boolean {
        val connectivityManager = getApplication<GameApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return  when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}