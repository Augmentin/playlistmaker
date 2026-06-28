package com.example.playlistmaker.sharing.domain.impl


import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    val content: Context
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
       return content.getString(R.string.share_text)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            content.getString(R.string.support_email),
            content.getString(R.string.support_subject),
            content.getString(R.string.support_body)
        )
    }

    private fun getTermsLink(): String {
        return  content.getString(R.string.practicum_offer_link)
    }
}