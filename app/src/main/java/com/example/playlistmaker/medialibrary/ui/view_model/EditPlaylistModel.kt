package com.example.playlistmaker.medialibrary.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import kotlinx.coroutines.launch

class EditPlaylistModel(
    private val playlistId: Long,
    saveFileInteractorImpl: SaveFileInteractor,
   playlistInteractor: PlaylistInteractor
) : NewPlaylistModel(saveFileInteractorImpl, playlistInteractor) {

    private val modelLiveData = MutableLiveData<PlaylistModel>()
    fun observeModel(): LiveData<PlaylistModel> = modelLiveData
    private var isImageChanged = false

    init {
        observePlaylist()
        saveStateLiveData.value = SavePlayListState.Editing
        stateLiveData.value = NewPlaylistState.DisableSave
    }
    override fun setName(name: String){
        this.playlistName = name.trim()
        updateSaveButtonState()
    }

    override fun setImage(image: Uri){
        this.playlistImage = image
        isImageChanged = image != modelLiveData.value?.imageUri
        updateSaveButtonState()
    }

    override fun setDescription(description: String){
        this.playlistDescription = description.trim()
        updateSaveButtonState()
    }



    override fun save(){
        if (stateLiveData.value !is NewPlaylistState.EnableSave) {
            return
        }
        stateLiveData.value  =  NewPlaylistState.DisableSave
        viewModelScope.launch {
            var fileName: String? = null
            try {
                val  oldImage = modelLiveData.value?.imageName
                if(isImageChanged){
                    fileName = playlistImage?.let { uri ->
                        saveFileInteractorImpl.saveToInternalStorage(uri)
                    }
                }else{
                    fileName = oldImage
                }


                val createdPlaylist = playlistInteractor.update(
                    PlaylistModel(
                        id = playlistId,
                        name = playlistName,
                        imageName = fileName,
                        description = playlistDescription
                            .trim()
                            .takeIf { it.isNotEmpty() },
                    )
                )
                if(isImageChanged){
                    oldImage?.let {
                        try {
                            saveFileInteractorImpl.deleteInternalStorage(fileName = it)
                        }catch (nothing: Exception){}
                    }
                }
                isImageChanged = false
                modelLiveData.value = createdPlaylist
                saveStateLiveData.value = SavePlayListState.Saved(
                    playlistId = createdPlaylist.id
                        ?: throw IllegalStateException("Room не вернул id"),
                    playlistName = createdPlaylist.name,
                )

            } catch (exception: Exception) {
                Log.e("NewPlaylistModel.save", exception.message ?: "Неизвестная ошибка")
                try {
                    fileName?.let{saveFileInteractorImpl.deleteInternalStorage(fileName = it)}
                }catch (nothing:Exception){}
                saveStateLiveData.value = SavePlayListState.Error(
                    message = "Не удалось редактировать плейлист"
                )
            }finally {
                stateLiveData.value  =  NewPlaylistState.EnableSave
            }
        }
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->  renderPlaylist(playlist) }
        }
    }

    fun renderPlaylist(playlist: PlaylistModel?){
        playlist.let {
            isImageChanged = false
            modelLiveData.value = it
            playlistName = it?.name ?: ""
            playlistDescription = it?.description ?: ""
            playlistImage = it?.imageUri
            stateLiveData.value = NewPlaylistState.DisableSave
            updateSaveButtonState()
        }
    }

    private fun updateSaveButtonState() {
        val original =  modelLiveData.value
        if (playlistName.isBlank()) {
            stateLiveData.value = NewPlaylistState.DisableSave
            return
        }

        val currentName = playlistName.trim()
        val originalName = original?.name?.trim()

        val currentDescription = playlistDescription.trim()
            .takeIf { it.isNotEmpty() }

        val originalDescription = original?.description?.trim()
            ?.takeIf { it.isNotEmpty() }

        val fieldsChanged =
            currentName != originalName ||
                    currentDescription != originalDescription ||
                    isImageChanged

        stateLiveData.value = if (fieldsChanged) {
            NewPlaylistState.EnableSave
        } else {
            NewPlaylistState.DisableSave
        }
    }
}