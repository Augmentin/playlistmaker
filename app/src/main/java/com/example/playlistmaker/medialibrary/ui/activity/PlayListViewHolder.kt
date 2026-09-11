package com.example.playlistmaker.medialibrary.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.playlistmaker.R

import com.example.playlistmaker.databinding.PlaylistItemBinding

import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel

class PlaylistViewHolder(private val binding: PlaylistItemBinding, private val saveFileInteractor: SaveFileInteractor) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            saveFileInteractor: SaveFileInteractor,
        ): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistItemBinding.inflate(
                inflater,
                parent,
                false
            )

            return PlaylistViewHolder(
                binding = binding,
                saveFileInteractor = saveFileInteractor,
            )
        }
    }
    fun bind(item: PlaylistModel) {


        binding.trackName.text = item.name.trim()
        binding.trackCount.text = item.trackCount.toString()

        val imageUri = item.imageName?.let { fileName ->
            saveFileInteractor.getFromInternalStorage(fileName)
        }

        Glide.with(binding.artwork)
            .load(imageUri)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .centerCrop()
            .into(binding.artwork)

    }


}