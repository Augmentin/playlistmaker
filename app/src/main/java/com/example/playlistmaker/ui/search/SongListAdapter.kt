package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackData


class SongListAdapter(private val data: List<TrackData>, private val onTrackClick: ((TrackData) -> Unit)? = null) :
    RecyclerView.Adapter<SongListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_cart, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SongListViewHolder,
        position: Int
    ) {
        holder.bind(data[position])

        if(onTrackClick != null){
            holder.itemView.setOnClickListener {
                onTrackClick(data[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }



}