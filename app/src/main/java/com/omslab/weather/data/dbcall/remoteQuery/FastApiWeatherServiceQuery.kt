// FastApiWeatherService.kt
package com.omslab.weather.data.dbcall.remoteQuery

import com.omslab.weather.data.models.FastApiWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FastApiWeatherServiceQuery {
    @GET("weather")
    suspend fun getWeatherFastAPI(
        @Query("city") city: String
    ): Response<FastApiWeatherModel>
}