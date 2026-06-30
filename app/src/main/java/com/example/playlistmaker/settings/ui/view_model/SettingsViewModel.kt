package com.example.playlistmaker.settings.ui.view_model


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator


import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.ThemeSettings

import com.example.playlistmaker.sharing.domain.impl.SharingInteractor
import com.example.playlistmaker.sharing.domain.models.EmailData


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val context: Context
) : ViewModel() {
    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer() {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as App)

                SettingsViewModel(Creator.provideSharingInteractor(app),
                    Creator.provideSettingsInteractor(app), app)
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
        sharingInteractor.shareApp(getShareAppLink())
    }

    fun openTerms(){
        sharingInteractor.openTerms(getTermsLink())
    }

    fun openSupport(){
        sharingInteractor.openSupport(getSupportEmailData())
    }


    private fun getShareAppLink(): String {
        return context.getString(R.string.share_text)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.support_email),
            context.getString(R.string.support_subject),
            context.getString(R.string.support_body)
        )
    }

    private fun getTermsLink(): String {
        return  context.getString(R.string.practicum_offer_link)
    }

}