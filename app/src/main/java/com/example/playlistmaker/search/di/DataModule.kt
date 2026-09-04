package com.example.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.preferences.PreferencesConstants
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ItunesClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<NetworkClient> {
        ItunesClient(
            get(),
            androidContext()
        )
    }

    factory { Gson() }

    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(
                PreferencesConstants.PLAYLISTMAKET_PREFERENCE,
                Context.MODE_PRIVATE
            )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db",
        )
            .addMigrations(
                AppDatabase.MIGRATION_1_2,
            )
            .build()
    }
}

