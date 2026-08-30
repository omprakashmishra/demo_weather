// FastAPIRemoteDataSource.kt
package com.omslab.weather.data.datasource

import com.omslab.weather.data.dbcall.remote.FastApiWeatherService
import com.omslab.weather.data.dbcall.network.ApiResult
import com.omslab.weather.data.dbcall.network.NetworkException
import com.omslab.weather.data.models.FastApiWeatherModel
import com.omslab.weather.data.models.WeatherModel
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FastApiWeatherRemoteDataSource @Inject constructor(
    private val fastApiWeatherService: FastApiWeatherService
) {
    suspend fun getWeather(city: String): ApiResult<FastApiWeatherModel> {
        return try {
            val response = fastApiWeatherService.getWeatherFastAPI(city)
            if (response.isSuccessful) {
                val body = response.body()
                if (body == null) {
                    ApiResult.Error(message = "Empty response from weather server.")
                } else if (!body.isValidResponse()) {
                    ApiResult.Error(message = "Invalid response from weather server.")
                } else {
                    ApiResult.Success(body)
                }
            } else {
                when (response.code()) {
                    404 -> ApiResult.Error(
                        code = 404,
                        message = NetworkException.CityNotFound().message
                    )
                    in 500..599 -> ApiResult.Error(
                        code = response.code(),
                        message = NetworkException.ServerUnavailable().message
                    )
                    else -> ApiResult.Error(
                        code = response.code(),
                        message = "Weather request failed."
                    )
                }
            }
        } catch (e: UnknownHostException) {
            ApiResult.NetworkError
        } catch (e: SocketTimeoutException) {
            ApiResult.Timeout
        } catch (e: IOException) {
            ApiResult.NetworkError
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Something went wrong. Please try again."
            )
        }
    }
}