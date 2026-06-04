package com.example.playlistmaker.presentation.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackData
import java.text.SimpleDateFormat
import java.util.Locale

class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivArtwork: ImageView = itemView.findViewById(R.id.artwork)
    private val tvTrackName: TextView = itemView.findViewById(R.id.trackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.artistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: TrackData) {

        tvTrackName.text = item.trackName?.trim() ?: "Undefined"
        tvArtistName.text = item.artistName?.trim() ?: "Undefined"
        tvTrackTime.text  = item.trackTimeMillis?.let {
            dateFormat.format(it)
        } ?: "00:00"

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