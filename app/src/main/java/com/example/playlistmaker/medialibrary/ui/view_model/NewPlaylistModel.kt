package com.example.playlistmaker.medialibrary.ui.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialibrary.domain.api.SaveFileInteractor
import com.example.playlistmaker.medialibrary.domain.model.PlaylistModel
import kotlinx.coroutines.launch

class NewPlaylistModel(val saveFileInteractorImpl: SaveFileInteractor, val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    private var name: String = ""
    private var image: Uri? = null
    private var description: String = ""

    fun isFieldsEmpty() : Boolean {
        return name.isBlank() && image == null && description.isBlank()
    }
    fun setName(name: String){
        this.name = name.trim()
        if(this.name.isNotBlank()){
            stateLiveData.postValue(NewPlaylistState.FilledRequiredFields)
        }else{
            stateLiveData.postValue(NewPlaylistState.EmptyRequiredFields)
        }
    }

    fun setImage(image: Uri){
        this.image = image
    }

    fun setDescription(description: String){
        this.description = description.trim()
    }

    fun save(){
        if (stateLiveData.value !is NewPlaylistState.FilledRequiredFields) {
            return
        }

        stateLiveData.value = NewPlaylistState.Saving

        viewModelScope.launch {
            var fileName: String? = null
            try {
                fileName = image?.let { uri ->
                    saveFileInteractorImpl.saveToInternalStorage(uri)
                }

                val createdPlaylist = playlistInteractor.create(
                    PlaylistModel(
                        id = null,
                        name = name,
                        imageName = fileName,
                        description = description
                            .trim()
                            .takeIf { it.isNotEmpty() },
                    )
                )

                stateLiveData.value = NewPlaylistState.Saved(
                    playlistId = createdPlaylist.id
                        ?: throw IllegalStateException("Room не вернул id"),
                    playlistName = createdPlaylist.name,
                )
            } catch (exception: Exception) {
                Log.e("NewPlaylistModel.save", exception.message ?: "Неизвестная ошибка")
                try {
                    fileName?.let{saveFileInteractorImpl.deleteInternalStorage(fileName = it)}
                }catch (nothing:Exception){}
                stateLiveData.value = NewPlaylistState.Error(
                    message = "Не удалось создать плейлист"
                )
            }
        }
    }

}