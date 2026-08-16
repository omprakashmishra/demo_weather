package com.omslab.weather.repo

import com.omslab.weather.data.models.WeatherDataModel
import com.omslab.weather.network.apiInput.ApiService
import retrofit2.Response
import javax.inject.Inject

// In WeatherRepository.kt
class WeatherRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWeatherByLocation(
        lat: String,
        lon: String,
        appId: String
    ): Response<WeatherDataModel> {
        return apiService.getWeatherByLocation(
            units = "metric",
            lat = lat,
            lon = lon,
            appId = appId
        )
    }
}