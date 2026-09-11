package com.example.playlistmaker.medialibrary.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel


class PlayListAdapter(
    private val onPlaylistClick: PlaylistListener,
    private val saveFileInteractor: SaveFileInteractor,
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlist = ArrayList<PlaylistModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder  = PlaylistViewHolder .from(parent, saveFileInteractor)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick.onSongClick(playlist[position])
        }
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    fun interface PlaylistListener {
        fun onSongClick(playlist: PlaylistModel)
    }

}