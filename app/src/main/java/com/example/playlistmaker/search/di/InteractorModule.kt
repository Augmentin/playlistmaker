package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }
}