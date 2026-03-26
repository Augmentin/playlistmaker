package com.example.playlistmaker


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.network.ItunesClient
import com.example.playlistmaker.search.SongListAdapter
import com.example.playlistmaker.network.itunes.SearchResponse
import com.example.playlistmaker.network.itunes.TrackData
import com.example.playlistmaker.search.SongListViewHolder
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback


class SearchActivity : AppCompatActivity() {

    private var current_search: String = STRING_DEF

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val STRING_DEF = ""
    }

    private lateinit var adapter: SongListAdapter
    private val trackList = mutableListOf<TrackData>()


    private lateinit var placeholderImg: ImageView
    private lateinit var placeholderTitle: TextView
    private lateinit var placeholderMessage: TextView
    private lateinit var refreshButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val currentView = findViewById<View>(R.id.search_view);
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        placeholderImg = findViewById(R.id.fail_img)
        placeholderTitle = findViewById(R.id.placeholderTitle)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        refreshButton = findViewById(R.id.refresh)

        adapter = SongListAdapter(trackList)
        val songItems = findViewById<RecyclerView>(R.id.songItems)
        songItems.adapter = adapter;
        val back = findViewById<MaterialToolbar>(R.id.back_toolbar)
        back.setNavigationOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setText(current_search)
        clearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
            inputEditText.setText("")
            inputEditText.clearFocus()
            inputEditText.setHint(R.string.search_hint)
            trackList.clear()
            adapter.notifyDataSetChanged()
        }
        refreshButton.setOnClickListener {
            hidePlaceholderFields()
            searchTrack()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hidePlaceholderFields()
                searchTrack()
            }
            false
        }

        inputEditText.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            if (!s.isNullOrEmpty()) {
                inputEditText.hint = ""
                current_search = s.toString()
            } else {
                current_search = STRING_DEF
            }
            clearButton.isVisible = !s.isNullOrEmpty()
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Log.i("onSaveInstanceState", current_search)
        outState.putString(SEARCH_STRING, current_search)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        current_search = savedInstanceState.getString(SEARCH_STRING, STRING_DEF)
    }


    private fun searchTrack() {
        ItunesClient.api.search(current_search).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse?>?,
                response: retrofit2.Response<SearchResponse?>?
            ) {
                if (response?.isSuccessful == true) {
                    val result: MutableList<TrackData> = response.body()?.results ?: mutableListOf()
                    if (result.isNotEmpty()) {
                        trackList.clear()
                        trackList.addAll(result)
                        adapter.notifyDataSetChanged()
                    } else {
                        showMessage(getString(R.string.not_found), "", R.drawable.not_found)
                    }
                } else {
                    showMessage(
                        getString(R.string.connect_problem_title),
                        getString(R.string.connect_problem_message),
                        R.drawable.connection_fail
                    )
                    refreshButton.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SearchResponse?>?, t: Throwable?) {
                showMessage(
                    getString(R.string.connect_problem_title),
                    getString(R.string.connect_problem_message),
                    R.drawable.connection_fail
                )
                refreshButton.visibility = View.VISIBLE
            }
        })
    }

    private fun hidePlaceholderFields() {
        placeholderTitle.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImg.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showMessage(
        textTitle: String,
        textMessage: String = "",
        imageResource: Int? = null
    ) {
        if (textTitle.isNotEmpty()) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderTitle.visibility = View.VISIBLE
            placeholderTitle.text = textTitle
            if (textMessage.isNotEmpty()) {
                placeholderMessage.text = textMessage
                placeholderMessage.visibility = View.VISIBLE
            }
            if (imageResource != null) {
                placeholderImg.setImageResource(imageResource)
                placeholderImg.visibility = View.VISIBLE
            }

        }
    }
}