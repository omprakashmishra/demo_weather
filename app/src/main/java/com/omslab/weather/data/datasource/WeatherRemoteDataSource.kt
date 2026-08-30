// WeatherRemoteDataSource.kt
package com.omslab.weather.data.datasource

import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.dbcall.remote.ApiService
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWeather(lat: String, lon: String): Result<com.omslab.weather.data.models.WeatherModel> {
        return try {
            val response = apiService.getWeatherByLocation(
                unit = "metric",
                lat = lat,
                lon = lon,
                appId = Constants.AppId
            )

            if (response.isSuccessful) {
                response.body()?.let { weatherModel ->
                    if (weatherModel.isValidResponse()) {
                        Result.success(weatherModel)
                    } else {
                        Result.failure(Exception("Invalid response from server"))
                    }
                } ?: Result.failure(Exception("Empty response from server"))
            } else {
                Result.failure(
                    Exception("API Error ${response.code()}: ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}