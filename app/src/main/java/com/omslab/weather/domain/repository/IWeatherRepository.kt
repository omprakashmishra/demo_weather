package com.omslab.weather.domain.repository

import com.omslab.weather.domain.models.Weather

interface IWeatherRepository {
    suspend fun getWeather(lat: String, lon: String): Result<Weather>
}