package com.example.playlistmaker
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        val back = findViewById<MaterialToolbar>(R.id.back_toolbar)
        back.setNavigationOnClickListener {
            finish()
        }
        val shareButton = findViewById<MaterialTextView>(R.id.share_button)
        shareButton.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            }
            val chooser = Intent.createChooser(sendIntent, getString(R.string.chooser_text))
            startActivity(chooser)
        }

        val supportButton = findViewById<MaterialTextView>(R.id.support_button)
        supportButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.support_subject)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.support_body)
                )
            }
            startActivity(intent)

        }
        val agreementButton = findViewById<MaterialTextView>(R.id.agreement_button)
        agreementButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = getString(R.string.practicum_offer_link).toUri()
            }

            startActivity(intent)
        }
    }
}