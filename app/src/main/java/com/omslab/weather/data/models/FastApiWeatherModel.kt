package com.omslab.weather.data.models

import com.google.gson.annotations.SerializedName

data class FastApiWeatherModel(
    @SerializedName("city")
    val city: String? = null,

    @SerializedName("temperature")
    val temperature: Int? = null,

    @SerializedName("condition")
    val condition: String? = null
) {
    /**
     * RESPONSE validation.
     *
     * Used after receiving the response from FastAPI.
     */
    fun isValidResponse(): Boolean {
        return !city.isNullOrBlank() &&
                temperature != null &&
                !condition.isNullOrBlank()
    }
}