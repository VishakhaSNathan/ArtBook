package com.example.artbook.api

import com.example.artbook.model.ImageResponse
import com.example.artbook.util.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApi {

    @GET("/api/")
    suspend fun Search(
        @retrofit2.http.Query(value = "q") SearchQuery : String,
        @retrofit2.http.Query(value = "key") apiKey : String = API_KEY
    ): Response<ImageResponse>
}