package com.example.playlistmaker.sharing.domain.impl


import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp(shareAppLink: String) {
        externalNavigator.shareLink(shareAppLink)
    }

    override fun openTerms(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(email: EmailData) {
        externalNavigator.openEmail(email)
    }


}