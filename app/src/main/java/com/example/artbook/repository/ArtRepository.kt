package com.example.artbook.repository

import androidx.lifecycle.LiveData
import com.example.artbook.api.RetrofitApi
import com.example.artbook.model.ImageResponse
import com.example.artbook.roomdb.Art
import com.example.artbook.roomdb.ArtDao
import com.example.artbook.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitApi: RetrofitApi
): ArtRepositoryInterface {

    override suspend fun insertArt(art: Art) {
        artDao.insert(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.delete(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitApi.Search(imageString)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error",null)
            }
            else
            {
                Resource.error("Error",null)
            }
        }
        catch (e: Exception){
            Resource.error("No data",null)
        }
    }
}