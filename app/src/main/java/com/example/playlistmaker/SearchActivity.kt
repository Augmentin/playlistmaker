package com.example.playlistmaker


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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
import com.example.playlistmaker.preferences.PreferencesConstants.PLAYLISTMAKET_PREFERENCE
import com.example.playlistmaker.search.SearchHistory
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback


class SearchActivity : AppCompatActivity() {

    private var current_search: String = STRING_DEF

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var adapter: SongListAdapter
    private val trackList = mutableListOf<TrackData>()

    private lateinit var searchHistory: SearchHistory

    private lateinit var placeholderImg: ImageView
    private lateinit var placeholderTitle: TextView
    private lateinit var placeholderMessage: TextView
    private lateinit var refreshButton: Button
    private lateinit var progressBar: ProgressBar

    private val searchRunnable = Runnable { searchTrack() }
    private lateinit var viewHistoryGroup: LinearLayout
    private lateinit var historyRefresh: Button

    private lateinit var inputEditText: EditText
    private lateinit var historyAdapter: SongListAdapter

    private lateinit var historySongItems: RecyclerView
    private lateinit var songItems: RecyclerView
    private var isClickAllowed = true


    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val currentView = findViewById<View>(R.id.search_view);
        ViewCompat.setOnApplyWindowInsetsListener(currentView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchHistory = SearchHistory(getSharedPreferences(PLAYLISTMAKET_PREFERENCE, MODE_PRIVATE))
        viewHistoryGroup = findViewById(R.id.searchHistoryGroup)
        placeholderImg = findViewById(R.id.fail_img)
        placeholderTitle = findViewById(R.id.placeholderTitle)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        refreshButton = findViewById(R.id.refresh)
        historyRefresh = findViewById(R.id.historyRefresh)
        progressBar = findViewById(R.id.progressBar)
        historySongItems = findViewById(R.id.historySongItems)
        adapter = SongListAdapter(trackList, { track ->
            if(clickDebounce()){
                searchHistory.addToHistory(track)
                val displayIntent = Intent(this, PlaylistActivity::class.java)
                displayIntent.putExtra("TRACK", Gson().toJson(track))
                startActivity(displayIntent)
            }
        })
        historyAdapter = SongListAdapter(searchHistory.historyArray, { track ->
            if(clickDebounce()){
                val displayIntent = Intent(this, PlaylistActivity::class.java)
                displayIntent.putExtra("TRACK", Gson().toJson(track))
                startActivity(displayIntent)
            }
        })
        songItems = findViewById<RecyclerView>(R.id.songItems)
        songItems.adapter = adapter
        historySongItems.adapter = historyAdapter
        val back = findViewById<MaterialToolbar>(R.id.back_toolbar)
        back.setNavigationOnClickListener {
            finish()
        }
        historyRefresh.setOnClickListener {
            searchHistory.clearHistory()
            historyAdapter.notifyDataSetChanged()
            viewHistoryGroup.isVisible = false
        }
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setText(current_search)

        clearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
            inputEditText.setText("")
            inputEditText.clearFocus()
            inputEditText.setHint(R.string.search_hint)
            trackList.clear()
            hidePlaceholderFields()
            adapter.notifyDataSetChanged()
        }
        refreshButton.setOnClickListener {
            searchTrack()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
            hidePlaceholderFields()
            viewHistoryGroup.isVisible = inputEditText.hasFocus() && s?.isEmpty() == true
            if(s?.isEmpty() == true){
               // songItems.isVisible = false
                handler.removeCallbacks(searchRunnable)
                trackList.clear()
                adapter.notifyDataSetChanged()
            }

            searchDebounce()

        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            viewHistoryGroup.isVisible =
                searchHistory.historyArray.size > 0 && hasFocus && inputEditText.text.isEmpty()
            historyAdapter.notifyDataSetChanged()
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
        if (!inputEditText.text.isNullOrEmpty()) {
            hidePlaceholderFields()
            viewHistoryGroup.isVisible = false
            progressBar.isVisible = true
            songItems.isVisible = false
            val text = inputEditText.text;
            ItunesClient.api.search(current_search).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse?>?,
                    response: retrofit2.Response<SearchResponse?>?
                ) {
                    if (response?.isSuccessful == true) {
                        if(text.equals(inputEditText.text)){
                            progressBar.isVisible = false
                            songItems.isVisible = true
                            val result: MutableList<TrackData> =
                                response.body()?.results ?: mutableListOf()
                            if (result.isNotEmpty()) {
                                trackList.clear()
                                trackList.addAll(result)
                                adapter.notifyDataSetChanged()
                            } else {
                                showMessage(getString(R.string.not_found), "", R.drawable.not_found)
                            }
                        }

                    } else {
                        progressBar.isVisible = false
                        showMessage(
                            getString(R.string.connect_problem_title),
                            getString(R.string.connect_problem_message),
                            R.drawable.connection_fail
                        )
                        refreshButton.isVisible = true
                    }
                }

                override fun onFailure(call: Call<SearchResponse?>?, t: Throwable?) {
                    progressBar.isVisible = false
                    songItems.isVisible = true
                    showMessage(
                        getString(R.string.connect_problem_title),
                        getString(R.string.connect_problem_message),
                        R.drawable.connection_fail
                    )
                    refreshButton.isVisible = true
                }
            })
        }

    }

    private fun hidePlaceholderFields() {
        if (placeholderTitle.visibility === View.VISIBLE) {
            placeholderTitle.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            placeholderImg.visibility = View.GONE
            refreshButton.visibility = View.GONE
        }
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