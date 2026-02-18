package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val currentView = findViewById<View>(R.id.search_view );
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val back = findViewById<MaterialToolbar>(R.id.back_toolbar)
        back.setNavigationOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
            inputEditText.setText("")
            inputEditText.clearFocus()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    inputEditText.hint = ""
                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboardAndClearFocus(editText: EditText) {
        // 1. Получаем InputMethodManager
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // 2. Скрываем клавиатуру
        imm.hideSoftInputFromWindow(editText.windowToken, 0)

        // 3. Убираем фокус с EditText
        editText.clearFocus()
    }
}