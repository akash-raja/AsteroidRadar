package com.example.asteroidradar.api

import com.example.asteroidradar.BuildConfig
import com.example.asteroidradar.PictureOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NASAService {

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") key: String = BuildConfig.ApiKey): Response<PictureOfDay>

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("start_date") date: String, @Query("api_key") key: String = BuildConfig.ApiKey): Response<String>
}