package com.example.playlistmaker.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.network.itunes.TrackData
import java.text.SimpleDateFormat
import java.util.Locale

class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivArtwork: ImageView = itemView.findViewById(R.id.artwork)
    private val tvTrackName: TextView = itemView.findViewById(R.id.trackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.artistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.trackTime)
    fun bind(item: TrackData) {

        tvTrackName.text = item.trackName?.trim() ?: "Undefined"
        tvArtistName.text = item.artistName?.trim() ?: "Undefined"
        tvTrackTime.text  = item.trackTimeDate?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        } ?: ""

        Glide.with(itemView).load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    itemView.context.resources
                        .getDimensionPixelSize(R.dimen.search_radius_image)
                )
            )
            .into(ivArtwork)

    }


}