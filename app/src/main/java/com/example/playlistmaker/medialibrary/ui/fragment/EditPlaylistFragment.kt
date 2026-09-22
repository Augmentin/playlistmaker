package com.example.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.ui.view_model.EditPlaylistModel
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistState
import com.example.playlistmaker.medialibrary.ui.view_model.SavePlayListState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel: EditPlaylistModel by viewModel {
        parametersOf(requireArguments().getLong(ARGS_PLAYLIST_ID))
    }
    companion object {
        private const val ARGS_PLAYLIST_ID = "EDIT_PLAYLIST_ID"
        fun createArgs(playlistId: Long): Bundle {
            return bundleOf(ARGS_PLAYLIST_ID to playlistId)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToolbar.title = getString(R.string.edit_title)
        binding.newPlaylist.text = getString(R.string.save)
        viewModel.observeModel().observe(viewLifecycleOwner){
            model ->
                model?.imageUri?.let {
                    binding.image.setImageURI(it)
                }
                model?.name?.let {
                    binding.nameEditText.setText(it)
                }
                model?.description?.let {
                    binding.descriptionEditText.setText(it)
                }
        }
    }

    override fun onSaved(state: SavePlayListState.Saved){
        findNavController().navigateUp()
    }
    override fun handleBackPressed(){
        findNavController().navigateUp()
    }
}