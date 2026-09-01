package com.omslab.weather.data.datasource

import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.dbcall.networkBase.NetworkException
import com.omslab.weather.data.dbcall.remoteQuery.FastApiWeatherServiceQuery
import com.omslab.weather.data.models.FastApiWeatherModel
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FastApiWeatherRemoteDataSource @Inject constructor(
    private val fastApiWeatherService: FastApiWeatherServiceQuery
) {

    /**
     * REQUEST
     * FastApiWeatherModel(city = "Delhi")
     *
     * RESPONSE
     * FastApiWeatherModel(
     *     city = "Delhi",
     *     temperature = 30,
     *     condition = "Sunny"
     * )
     */
    suspend fun getWeather(
        request: FastApiWeatherModel
    ): ApiResult<FastApiWeatherModel> {

        val city = request.city?.trim().orEmpty()

        if (city.isEmpty()) {
            return ApiResult.Error(
                message = "City cannot be empty."
            )
        }

        return try {

            // Request: GET /weather?city=Delhi
            val response = fastApiWeatherService
                .getWeatherFastAPI(city)

            if (response.isSuccessful) {

                val body = response.body()

                when {
                    body == null -> {
                        ApiResult.Error(
                            message = "Empty response from weather server."
                        )
                    }

                    !body.isValidResponse() -> {
                        ApiResult.Error(
                            message = "Invalid response from weather server."
                        )
                    }

                    else -> {
                        ApiResult.Success(body)
                    }
                }

            } else {

                when (response.code()) {

                    404 -> {
                        ApiResult.Error(
                            code = 404,
                            message = NetworkException
                                .CityNotFound()
                                .message
                        )
                    }

                    in 500..599 -> {
                        ApiResult.Error(
                            code = response.code(),
                            message = NetworkException
                                .ServerUnavailable()
                                .message
                        )
                    }

                    else -> {
                        ApiResult.Error(
                            code = response.code(),
                            message = "Weather request failed."
                        )
                    }
                }
            }

        } catch (_: UnknownHostException) {

            ApiResult.NetworkError

        } catch (_: SocketTimeoutException) {

            ApiResult.Timeout

        } catch (_: IOException) {

            ApiResult.NetworkError

        } catch (e: Exception) {

            ApiResult.Error(
                message = e.message
                    ?: "Something went wrong. Please try again."
            )
        }
    }
}