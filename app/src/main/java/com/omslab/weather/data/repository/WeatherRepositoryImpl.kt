package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.WeatherRemoteDataSource
import com.omslab.weather.data.models.WeatherModel
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) {

    suspend fun getWeather(lat: String, lon: String): Result<WeatherModel> {
        return if (lat.isEmpty() || lon.isEmpty()) {
            Result.failure(IllegalArgumentException("Latitude and Longitude cannot be empty"))
        } else {
            remoteDataSource.getWeather(lat, lon)
        }
    }
}