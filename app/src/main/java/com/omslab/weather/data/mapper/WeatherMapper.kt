package com.omslab.weather.data.mapper

import com.omslab.weather.data.models.WeatherDataModel
import com.omslab.weather.domain.models.Weather

object WeatherMapper {
    fun mapToDomain(weatherData: WeatherDataModel): Weather {
        return Weather(
            id = weatherData.id,
            cityName = weatherData.name,
            temperature = weatherData.main.temp,
            feelsLike = weatherData.main.feels_like,
            humidity = weatherData.main.humidity,
            pressure = weatherData.main.pressure,
            windSpeed = weatherData.wind.speed,
            weatherDescription = weatherData.weather.firstOrNull()?.description ?: "",
            weatherIcon = weatherData.weather.firstOrNull()?.icon ?: "",
            lat = weatherData.coord.lat,
            lon = weatherData.coord.lon,
            country = weatherData.sys.country,
            sunrise = weatherData.sys.sunrise,
            sunset = weatherData.sys.sunset,
            timestamp = weatherData.dt
        )
    }
}