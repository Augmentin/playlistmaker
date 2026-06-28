package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.models.EmailData

interface SharingInteractor {
    fun shareApp(shareAppLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(email: EmailData)
}