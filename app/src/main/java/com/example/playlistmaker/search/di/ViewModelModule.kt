package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.view_model.HistoryViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(androidContext(), get(), get())
    }

    viewModel {
        HistoryViewModel( get(), get())
    }
}