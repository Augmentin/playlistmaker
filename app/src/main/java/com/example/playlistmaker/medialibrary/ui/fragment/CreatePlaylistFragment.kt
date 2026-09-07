package com.example.playlistmaker.medialibrary.ui.fragment


import android.os.Bundle

import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentCreateplaylistBinding
import com.example.playlistmaker.medialibrary.ui.view_model.NewPlaylistModel

import org.koin.androidx.viewmodel.ext.android.viewModel

import kotlin.getValue

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreateplaylistBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModel<NewPlaylistModel>()
    private val pickMedia = registerForActivityResult(
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
            findNavController().navigateUp()
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

        binding.newPlaylist.setOnClickListener {
            viewModel.save()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PHOTO_PICKER_TAG = "PhotoPicker"

    }
}