package com.example.playlistmaker.settings.ui.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator


import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.ThemeSettings

import com.example.playlistmaker.sharing.domain.impl.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer() {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)

                SettingsViewModel(Creator.provideSharingInteractor(app),
                    Creator.provideSettingsInteractor(app))
            }
        }
    }


    fun isThemeDark() : Boolean{
        return settingsInteractor.getThemeSettings().dark
    }
    fun changeTheme(dark: Boolean){
        settingsInteractor.updateThemeSetting(ThemeSettings(dark))
    }

    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun openTerms(){
        sharingInteractor.openTerms()
    }

    fun openSupport(){
        sharingInteractor.openSupport()
    }
}