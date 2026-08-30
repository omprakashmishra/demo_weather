// FastApiWeatherService.kt
package com.omslab.weather.data.dbcall.remote

import com.omslab.weather.data.models.FastApiWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FastApiWeatherService {
    @GET("weather")
    suspend fun getWeatherFastAPI(
        @Query("city") city: String
    ): Response<FastApiWeatherModel>
}