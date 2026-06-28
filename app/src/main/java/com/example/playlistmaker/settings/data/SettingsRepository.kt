package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}