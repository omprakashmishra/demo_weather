package com.omslab.weather.domain.usecase.location

import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FastApiWeatherModel
import com.omslab.weather.data.repository.FastApiWeatherRepositoryImpl
import javax.inject.Inject

class GetWeatherFastApiUseCase @Inject constructor(
    private val repository: FastApiWeatherRepositoryImpl
) {

    suspend operator fun invoke(
        request: FastApiWeatherModel
    ): ApiResult<FastApiWeatherModel> {
        return repository.getWeather(request)
    }
}