package com.example.playlistmaker.medialibrary.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import androidx.core.view.isVisible

class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistItemBinding.inflate(
                inflater,
                parent,
                false
            )

            return PlaylistViewHolder(
                binding = binding,
            )
        }
    }
    fun bind(item: PlaylistModel) {


        binding.trackName.text = item.name.trim()
        binding.trackCount.text = binding.root.resources.getQuantityString(
                R.plurals.playlist_track_count,
        item.trackCount,
        item.trackCount,
        )

        if (item.imageUri == null) {
            Glide.with(binding.artwork).clear(binding.artwork)
            binding.artwork.apply {
                scaleType = ImageView.ScaleType.CENTER
                setImageResource(R.drawable.placeholder_l)
            }
        } else {
            binding.artwork.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            Glide.with(binding.artwork)
                .load(item.imageUri)
                .into(binding.artwork)
        }


    }


}