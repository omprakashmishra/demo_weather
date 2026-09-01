package com.omslab.weather.data.dbcall.networkBase

sealed class NetworkException(
    override val message: String
) : Exception(message) {

    class NoInternet : NetworkException(
        "No internet connection. Please check your network."
    ) {
        private fun readResolve(): Any = NoInternet()
    }

    class Timeout : NetworkException(
        "Request timed out. Please try again."
    ) {
        private fun readResolve(): Any = Timeout()
    }

    class CityNotFound : NetworkException(
        "City not found."
    ) {
        private fun readResolve(): Any = CityNotFound()
    }

    class ServerUnavailable : NetworkException(
        "Weather server is currently unavailable."
    ) {
        private fun readResolve(): Any = ServerUnavailable()
    }

    data class ApiError(
        val code: Int
    ) : NetworkException(
        "Weather request failed. Error code: $code"
    )
}