package com.omslab.weather.domain.usecase.weather

import com.omslab.weather.data.models.WeatherModel
import com.omslab.weather.data.repository.WeatherRepositoryImpl
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepositoryImpl
) {
    suspend operator fun invoke(lat: String, lon: String): Result<WeatherModel> =
        repository.getWeather(lat, lon)
}
