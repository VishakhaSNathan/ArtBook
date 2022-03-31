package com.example.artbook.viewmodel

import android.content.ClipDescription
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artbook.model.ImageResponse
import com.example.artbook.repository.ArtRepositoryInterface
import com.example.artbook.roomdb.Art
import com.example.artbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    val repository: ArtRepositoryInterface
) : ViewModel() {

    //art fragment
    var artList = repository.getArt()

    //image api
    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList : LiveData<Resource<ImageResponse>>
       get() = images

    //search image
    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl : LiveData<String>
    get() = selectedImage

    //art detail fragment
    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>>
    get() = insertArtMsg

    fun resetArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImageUrl(url: String){
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art)= viewModelScope.launch{
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun searchForImage(searchString: String){
        if(searchString.isEmpty()){
            return
        }
        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
        }
    }

    fun makeArt(artName: String, artistName: String, year: String, description: String){
        if(artName.isEmpty() || artistName.isEmpty() || year.isEmpty()|| description.isEmpty()){
            insertArtMsg.postValue(Resource.error("Enter all fields",null))
            return
        }
        val yearInt =try{
            year.toInt()
        }
        catch (e: Exception){
            insertArtMsg.postValue(Resource.error("Year should be a number",null))
            return
        }
        val art = Art(artName,artistName,yearInt,description,selectedImage.value ?: "")
        insertArt(art)
        setSelectedImageUrl("")
        insertArtMsg.postValue(Resource.success(art))

    }

}