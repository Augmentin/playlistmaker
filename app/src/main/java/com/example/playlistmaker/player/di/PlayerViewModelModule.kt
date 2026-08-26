package com.example.playlistmaker.player.di



import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.domain.models.TrackData

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel { (trackData: TrackData) ->
        PlayerViewModel(trackData, get(), get())
    }

    factory {
        MediaPlayer()
    }
}