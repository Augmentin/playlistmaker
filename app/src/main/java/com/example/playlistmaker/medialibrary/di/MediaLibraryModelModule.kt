package com.example.playlistmaker.medialibrary.di



import com.example.playlistmaker.db.data.PlaylistRepositoryImpl
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.db.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.medialibrary.data.SaveFileRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileRepository
import com.example.playlistmaker.medialibrary.domain.impl.SaveFileInteractorImpl
import com.example.playlistmaker.medialibrary.ui.view_model.FavouritesModel
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistModel
import com.example.playlistmaker.medialibrary.ui.view_model.PlaylistsModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val medialibraryViewModelModule = module {

    viewModel {
        FavouritesModel(get())
    }

    viewModel {
        PlaylistsModel(get())
    }

    viewModel<NewPlaylistModel>(){
        NewPlaylistModel(get(), get())
    }


    factory <SaveFileInteractor>{
        SaveFileInteractorImpl(get())
    }

    single<SaveFileRepository>{
        SaveFileRepositoryImpl(androidContext())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory<PlaylistInteractor>{
        PlaylistInteractorImpl(get())
    }
}