package com.example.playlistmaker.search.di

import com.example.playlistmaker.db.data.FavoritesTracksRepositoryImpl
import com.example.playlistmaker.db.data.convertors.TrackDbConvertors
import com.example.playlistmaker.db.domain.api.FavoritesTracksRepository
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {


    single<HistoryRepository> {
        HistoryRepositoryImpl(
            get(),
            get()
        )
    }
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory { TrackDbConvertors() }

    single<FavoritesTracksRepository>{
        FavoritesTracksRepositoryImpl(get(), get())
    }
}