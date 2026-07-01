package com.example.playlistmaker.search.di

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
}