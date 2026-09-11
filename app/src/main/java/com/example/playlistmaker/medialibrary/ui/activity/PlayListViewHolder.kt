package com.example.playlistmaker.medialibrary.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.PlaylistItemBinding


import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

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
        binding.trackCount.text = item.getTrackCountText()


        Glide.with(binding.artwork)
            .load(item.imageUri)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .centerCrop()
            .into(binding.artwork)

    }


}