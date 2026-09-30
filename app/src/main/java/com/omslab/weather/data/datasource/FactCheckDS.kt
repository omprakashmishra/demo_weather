package com.omslab.weather.data.datasource

import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.dbcall.remoteQuery.FastApiWeatherServiceQuery
import com.omslab.weather.data.models.FactCheckModel
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactCheckDS @Inject constructor(
    private val service: FastApiWeatherServiceQuery
) {

    suspend fun factCheck(request: FactCheckModel): ApiResult<FactCheckModel> {
        val claim = request.claim.trim()
        if (claim.isEmpty()) {
            return ApiResult.Error(message = "Claim cannot be empty.")
        }

        return try {
            val response = service.factCheckAPI(request.copy(claim = claim))
            response.toApiResult()
        } catch (_: UnknownHostException) {
            ApiResult.NetworkError
        } catch (_: SocketTimeoutException) {
            ApiResult.Timeout
        } catch (_: IOException) {
            ApiResult.NetworkError
        } catch (e: Exception) {
            ApiResult.Error(
                message = e.message ?: "Something went wrong. Please try again."
            )
        }
    }

    // ---------------- Mapping helpers ----------------

    private fun Response<FactCheckModel>.toApiResult(): ApiResult<FactCheckModel> =
        if (isSuccessful) successResult() else httpErrorResult()

    private fun Response<FactCheckModel>.successResult(): ApiResult<FactCheckModel> {
        val body = body()
        return when {
            body == null -> ApiResult.Error(
                message = "Empty response from server."
            )
            else -> ApiResult.Success(body)
        }
    }

    private fun Response<FactCheckModel>.httpErrorResult(): ApiResult.Error {
        val serverMessage = errorBody()?.string()?.takeIf { it.isNotBlank() }
        return when (code()) {
            400 -> ApiResult.Error(400, serverMessage ?: "Bad request.")
            401, 403 -> ApiResult.Error(code(), "Not authorized.")
            404 -> ApiResult.Error(404, "Fact-check endpoint not found.")
            408 -> ApiResult.Error(408, "Request timed out.")
            429 -> ApiResult.Error(429, "Too many requests. Try again later.")
            in 500..599 -> ApiResult.Error(code(), "Server unavailable. Try again later.")
            else -> ApiResult.Error(code(), serverMessage ?: "Request failed (${code()}).")
        }
    }
}