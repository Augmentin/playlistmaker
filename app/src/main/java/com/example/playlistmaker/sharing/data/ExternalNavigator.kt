package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigator(val content: Context) {

    fun shareLink(link: String){
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT,link)
        }
        val chooser = Intent.createChooser(sendIntent, content.getString(R.string.chooser_text))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        content.startActivity(chooser)
    }

    fun openLink(link: String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = link.toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        }
        content.startActivity(intent)
    }

    fun openEmail(email: EmailData){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email.to))
            putExtra(
                Intent.EXTRA_SUBJECT,
                email.subject
            )
            putExtra(
                Intent.EXTRA_TEXT,
                email.body
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        content.startActivity(intent)
    }
}