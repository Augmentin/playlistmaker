package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.NetworkConstants
import com.example.playlistmaker.search.data.network.CustomDateTypeAdapter
import com.example.playlistmaker.search.data.network.ItunesApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

val networkModule = module {


    single<ItunesApiService>{
        get<Retrofit>().create(ItunesApiService::class.java)
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.ITUNES_BASE_URL)
            .client(get())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
                        .create()
                )
            )
            .build()
    }

    factory<OkHttpClient>{
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }
}