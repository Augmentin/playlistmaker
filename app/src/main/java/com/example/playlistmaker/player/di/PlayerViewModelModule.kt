package com.example.playlistmaker.player.di



import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.TrackToPlaylistModel
import com.example.playlistmaker.search.domain.models.TrackData

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel { (trackData: TrackData) ->
        PlayerViewModel(trackData, get(), get())
    }
    viewModel { (trackData: TrackData) ->
        TrackToPlaylistModel(trackData = trackData, playlistInteractor = get())
    }
    factory {
        MediaPlayer()
    }
}