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

open class NewPlaylistModel(val saveFileInteractorImpl: SaveFileInteractor, val playlistInteractor: PlaylistInteractor): ViewModel() {

    protected val stateLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = stateLiveData
    protected val saveStateLiveData = MutableLiveData<SavePlayListState>()
    fun observeSaveState(): LiveData<SavePlayListState> = saveStateLiveData

    init {
        saveStateLiveData.value = SavePlayListState.Editing
        stateLiveData.value = NewPlaylistState.DisableSave
    }
    protected var playlistName: String = ""
    protected var playlistImage: Uri? = null
    protected var playlistDescription: String = ""

    fun isFieldsEmpty() : Boolean {
        return playlistName.isBlank() && playlistImage == null && playlistDescription.isBlank()
    }
    open fun setName(name: String){
        this.playlistName = name.trim()
        if(this.playlistName.isNotBlank()){
            stateLiveData.postValue(NewPlaylistState.EnableSave)
        }else{
            stateLiveData.postValue(NewPlaylistState.DisableSave)
        }
    }

    open fun setImage(image: Uri){
        this.playlistImage = image
    }

    open fun setDescription(description: String){
        this.playlistDescription = description.trim()
    }

    open fun save(){
        if (stateLiveData.value !is NewPlaylistState.EnableSave) {
            return
        }

        stateLiveData.value  =  NewPlaylistState.DisableSave

        viewModelScope.launch {
            var fileName: String? = null
            try {
                fileName = playlistImage?.let { uri ->
                    saveFileInteractorImpl.saveToInternalStorage(uri)
                }

                val createdPlaylist = playlistInteractor.create(
                    PlaylistModel(
                        id = null,
                        name = playlistName,
                        imageName = fileName,
                        description = playlistDescription
                            .trim()
                            .takeIf { it.isNotEmpty() },
                    )
                )

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
                    message = "Не удалось создать плейлист"
                )
            }finally {
                stateLiveData.value  =  NewPlaylistState.EnableSave
            }
        }
    }

}