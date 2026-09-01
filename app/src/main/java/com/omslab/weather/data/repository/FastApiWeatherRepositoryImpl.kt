package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.FastApiWeatherRemoteDataSource
import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FastApiWeatherModel
import javax.inject.Inject

class FastApiWeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: FastApiWeatherRemoteDataSource
) {

    suspend fun getWeather(
        request: FastApiWeatherModel
    ): ApiResult<FastApiWeatherModel> {

        return remoteDataSource.getWeather(request)
    }
}