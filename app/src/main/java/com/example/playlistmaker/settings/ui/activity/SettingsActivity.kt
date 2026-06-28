package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.preferences.PreferencesConstants

import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.settingsView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModel = ViewModelProvider(this, SettingsViewModel.getFactory())
            .get(SettingsViewModel::class.java)
        binding.themeSwitcher.isChecked = viewModel.isThemeDark()


        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked  ->
            viewModel.changeTheme(checked)
        }


        binding.backToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportButton.setOnClickListener {
            viewModel.openSupport()
        }
        binding.agreementButton.setOnClickListener {
            viewModel.openTerms()
        }
    }
}