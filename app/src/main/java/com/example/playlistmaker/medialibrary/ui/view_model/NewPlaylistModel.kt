package com.example.playlistmaker.medialibrary.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPlaylistModel: ViewModel() {

    private val stateLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    private var name: String = ""
    private var image: Uri? = null
    private var description: String = ""


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
        this.description = description
    }

    fun save(){
        if(stateLiveData.value is NewPlaylistState.FilledRequiredFields){

        }
    }

}