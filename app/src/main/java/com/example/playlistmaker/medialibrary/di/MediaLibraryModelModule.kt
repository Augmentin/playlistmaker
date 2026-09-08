package com.example.playlistmaker.medialibrary.di



import com.example.playlistmaker.medialibrary.data.SaveFileRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository
import com.example.playlistmaker.medialibrary.domain.impl.SaveFileInteractorImpl
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesModel
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistModel
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

    viewModel<NewPlaylistModel>(){
        NewPlaylistModel(get())
    }


    factory <SaveFileInteractor>{
        SaveFileInteractorImpl(get())
    }

    single<SaveFileRepository>{
        SaveFileRepositoryImpl(androidContext())
    }
}