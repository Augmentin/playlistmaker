package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_btn)
        searchButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Поиск\"", Toast.LENGTH_SHORT).show()
        }
        val mediaButton = findViewById<Button>(R.id.media_btn)
        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Медиатека\"", Toast.LENGTH_SHORT).show()
        }
        val settingButton = findViewById<Button>(R.id.settings_btn)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на кнопку \"Настройки\"", Toast.LENGTH_SHORT)
                .show()
        } }
        settingButton.setOnClickListener(imageClickListener)

    }
}