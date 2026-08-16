package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.WeatherRemoteDataSource
import com.omslab.weather.domain.models.Weather
import com.omslab.weather.domain.repository.IWeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : IWeatherRepository {

    override suspend fun getWeather(lat: String, lon: String): Result<Weather> {
        return if (lat.isEmpty() || lon.isEmpty()) {
            Result.failure(IllegalArgumentException("Latitude and Longitude cannot be empty"))
        } else {
            remoteDataSource.getWeather(lat, lon)
        }
    }
}