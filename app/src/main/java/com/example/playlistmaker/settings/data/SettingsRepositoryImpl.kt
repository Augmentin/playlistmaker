package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.preferences.PreferencesConstants
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.ThemeSettings

class SettingsRepositoryImpl(val content: Context): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        val sharedPrefs = content.getSharedPreferences(PreferencesConstants.PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        val darkTheme = sharedPrefs.getBoolean(PreferencesConstants.PLAYLISTMAKET_THEME_KEY, false)
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        val sharedPrefs = content.getSharedPreferences(PreferencesConstants.PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(PreferencesConstants.PLAYLISTMAKET_THEME_KEY, settings.dark)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (settings.dark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}