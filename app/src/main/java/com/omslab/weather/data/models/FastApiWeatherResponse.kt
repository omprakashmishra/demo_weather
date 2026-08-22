package com.omslab.weather.data.models

data class FastApiWeatherResponse(
    val city: String,
    val temperature: Int,
    val condition: String
)