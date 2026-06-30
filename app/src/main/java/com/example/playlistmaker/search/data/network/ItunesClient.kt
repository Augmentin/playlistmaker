package com.example.playlistmaker.search.data.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.NetworkConstants
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class ItunesClient(
    private val itunesService: ItunesApiService,
    private val context: Context
) : NetworkClient {


    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            Log.e("ItunesClient", "No internet connection")
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        try {
            val response = itunesService.search(dto.expression).execute()
            val body = response.body()
            return if (body != null) {
                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = response.code() }
            }
        }catch (e: Exception){
            Log.e("ItunesClient", "Network is unavailable or a connection error has occurred", e)
            return Response().apply { resultCode = -1 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}