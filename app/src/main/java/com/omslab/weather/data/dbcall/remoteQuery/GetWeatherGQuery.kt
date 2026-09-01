// ApiService.kt
package com.omslab.weather.data.dbcall.remoteQuery

import com.omslab.weather.data.models.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetWeatherGQuery {
    @GET("weather")
    suspend fun getWeatherByLocation(
        @Query("units") unit: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String
    ): Response<WeatherModel>  // Using unified model
}