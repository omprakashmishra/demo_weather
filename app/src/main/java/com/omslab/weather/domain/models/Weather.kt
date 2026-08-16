package com.omslab.weather.domain.models

data class Weather(
    val id: Int,
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val weatherDescription: String,
    val weatherIcon: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long
)