package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.dataModule
import com.example.playlistmaker.search.di.interactorModule
import com.example.playlistmaker.search.di.networkModule
import com.example.playlistmaker.search.di.repositoryModule
import com.example.playlistmaker.search.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule,
                networkModule,
                playerViewModelModule
            )
        }

        val interactor = Creator.provideSettingsInteractor(this)
        interactor.updateThemeSetting(interactor.getThemeSettings())

    }
}