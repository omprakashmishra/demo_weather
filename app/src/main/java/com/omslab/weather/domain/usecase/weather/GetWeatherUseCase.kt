package com.omslab.weather.domain.usecase.weather

import com.omslab.weather.domain.models.Weather
import com.omslab.weather.domain.repository.IWeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) {
    suspend operator fun invoke(
        lat: String,
        lon: String
    ): Result<Weather> {
        return repository.getWeather(lat, lon)
    }
}