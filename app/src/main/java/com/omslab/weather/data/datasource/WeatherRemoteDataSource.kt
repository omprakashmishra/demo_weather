// WeatherRemoteDataSource.kt
package com.omslab.weather.data.datasource

import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.dbcall.remoteQuery.GetWeatherGQuery
import com.omslab.weather.data.models.WeatherModel
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val getWeatherGQuery: GetWeatherGQuery
) {
    suspend fun getWeather(lat: String, lon: String): Result<WeatherModel> {
        return try {
            val response = getWeatherGQuery.getWeatherByLocation(
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