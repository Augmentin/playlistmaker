package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesModel
import com.example.playlistmaker.medialibrary.ui.view_model.PlayListModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val medialibraryViewModelModule = module {

    viewModel {
        FavouritesModel(get())
    }

    viewModel {
        PlayListModel(androidContext())
    }
}