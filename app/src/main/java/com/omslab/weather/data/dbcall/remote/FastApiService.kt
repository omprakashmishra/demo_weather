package com.omslab.weather.data.dbcall.remote

import com.omslab.weather.data.models.FastApiWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FastApiService {

    @GET("weather")
    suspend fun getWeather(
        @Query("city") city: String
    ): Response<FastApiWeatherResponse>
}