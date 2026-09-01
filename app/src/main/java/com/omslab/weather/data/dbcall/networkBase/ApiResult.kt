package com.omslab.weather.data.dbcall.networkBase

sealed class ApiResult<out T> {

    data class Success<T>(
        val data: T
    ) : ApiResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String
    ) : ApiResult<Nothing>()

    object NetworkError : ApiResult<Nothing>()

    object Timeout : ApiResult<Nothing>()
}