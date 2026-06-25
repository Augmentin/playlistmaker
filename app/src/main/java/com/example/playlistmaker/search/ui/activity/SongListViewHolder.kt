package com.example.playlistmaker.search.ui.activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SongCartBinding
import com.example.playlistmaker.search.domain.models.TrackData
import java.text.SimpleDateFormat
import java.util.Locale

class SongListViewHolder(private val binding: SongCartBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): SongListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SongCartBinding.inflate(inflater, parent, false)
            return SongListViewHolder(binding)
        }
    }
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: TrackData) {

        binding.trackName.text = item.trackName?.trim() ?: "Undefined"
        binding.artistName.text = item.artistName?.trim() ?: "Undefined"
        binding.trackTime.text  = item.trackTimeMillis?.let {
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
            .into( binding.artwork)

    }


}