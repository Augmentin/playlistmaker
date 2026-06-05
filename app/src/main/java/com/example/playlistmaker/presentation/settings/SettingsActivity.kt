package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.preferences.PreferencesConstants
import com.example.playlistmaker.presentation.App
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val sharedPrefs = getSharedPreferences(PreferencesConstants.PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE)
        themeSwitcher.isChecked = sharedPrefs.getBoolean(PreferencesConstants.PLAYLISTMAKET_THEME_KEY, false)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked  ->
            (applicationContext as App).switchTheme(checked)
        }

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