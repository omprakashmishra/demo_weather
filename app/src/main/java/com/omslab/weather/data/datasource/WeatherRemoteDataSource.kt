package com.omslab.weather.data.datasource

import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.mapper.WeatherMapper
import com.omslab.weather.data.dbcall.remote.ApiService
import com.omslab.weather.domain.models.Weather
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWeather(lat: String, lon: String): Result<Weather> {
        return try {
           val response=apiService.getWeatherByLocation(
                unit = "metric",
                lat = lat,
                lon = lon,
                appId = Constants.AppId
            )
            if (response.isSuccessful) {
                response.body()?.let { weatherData ->
                    Result.success(WeatherMapper.mapToDomain(weatherData))
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