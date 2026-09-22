package com.example.playlistmaker.medialibrary.ui.fragment


import android.os.Bundle

import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.playlistmaker.R
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentCreateplaylistBinding
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistModel
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistState
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import org.koin.androidx.viewmodel.ext.android.viewModel

import kotlin.getValue
import androidx.activity.addCallback
import com.example.playlistmaker.medialibrary.ui.view_model.SavePlayListState

open class CreatePlaylistFragment : Fragment() {

    protected var _binding: FragmentCreateplaylistBinding? = null
    protected val binding get() = _binding!!


    protected open val viewModel by viewModel<NewPlaylistModel>()
    protected val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            Log.d(PHOTO_PICKER_TAG, "Фотография не выбрана")
            return@registerForActivityResult
        }
        viewModel.setImage(uri)
        binding.image.setImageURI(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCreateplaylistBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.backToolbar.setOnClickListener {
            handleBackPressed()
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                handleBackPressed()
            }


        binding.image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylist.setOnClickListener {
            viewModel.save()
        }

        binding.nameEditText.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            viewModel.setName(s.toString())
        }
        binding.descriptionEditText.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            viewModel.setDescription(s.toString())
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
        viewModel.observeSaveState().observe(viewLifecycleOwner){
            renderSaveState(it)
        }
    }

    protected fun renderSaveState(state: SavePlayListState){
        when (state) {
            is SavePlayListState.Saved -> {
                onSaved(state)
            }
            is SavePlayListState.Error -> {
                binding.newPlaylist.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            else -> {}
        }
    }
    protected fun renderState(state: NewPlaylistState) {
        when (state) {
            NewPlaylistState.DisableSave -> {
                binding.newPlaylist.isEnabled = false
            }
            NewPlaylistState.EnableSave -> {
                binding.newPlaylist.isEnabled = true
            }

        }
    }

    protected open fun onSaved(state:  SavePlayListState.Saved){
        findNavController().previousBackStackEntry
            ?.savedStateHandle
            ?.set(
                CREATED_PLAYLIST_NAME_KEY,
                state.playlistName
            )

        findNavController().navigateUp()
    }

    protected open fun handleBackPressed(){
        if(viewModel.isFieldsEmpty()){
            findNavController().navigateUp()
        }else{
            val confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_medialib_title))
                .setMessage(R.string.dialog_medialib_message)
                .setNeutralButton(R.string.dialog_medialib_neutral) { dialog, which ->
                }.setPositiveButton(R.string.dialog_medialib_positive) { dialog, which ->
                    findNavController().navigateUp()
                }
            confirmDialog.show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PHOTO_PICKER_TAG = "PhotoPicker"
        const val CREATED_PLAYLIST_NAME_KEY = "created_playlist_name"
    }
}