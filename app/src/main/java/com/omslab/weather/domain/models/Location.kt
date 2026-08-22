package com.omslab.weather.domain.models

data class Location(
    val id: Int? = null,
    val lat: String,
    val lon: String,
    val cityName: String? = null,
    val country: String? = null,
    val temperature: String? = null,
    val description: String? = null,
    val icon: String? = null,
    val sunrise: String? = null,
    val sunset: String? = null,
    val entryDateTime: String? = null
)