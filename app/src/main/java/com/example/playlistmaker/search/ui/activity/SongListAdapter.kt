package com.example.playlistmaker.search.ui.activity


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.search.domain.models.TrackData


class SongListAdapter(val clickListener: SongClickListener) :
    RecyclerView.Adapter<SongListViewHolder>() {
    var trackList = ArrayList<TrackData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongListViewHolder = SongListViewHolder.from(parent)

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            clickListener.onSongClick(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun interface SongClickListener {
        fun onSongClick(movie: TrackData)
    }

}