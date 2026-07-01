package com.example.playlistmaker.player.di



import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.PlayerViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel { (url: String) ->
        PlayerViewModel(url, get())
    }

    factory {
        MediaPlayer()
    }
}