package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }
}