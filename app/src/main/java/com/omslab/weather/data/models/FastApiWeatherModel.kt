// FastApiWeatherModel.kt
package com.omslab.weather.data.models

import com.google.gson.annotations.SerializedName

/**
 * Unified FastAPI Weather Model
 */
data class FastApiWeatherModel(
    // Response fields
    @SerializedName("city")
    val city: String? = null,

    @SerializedName("temperature")
    val temperature: Int? = null,

    @SerializedName("condition")
    val condition: String? = null,

    // Request fields
    @SerializedName("city_query")
    val cityQuery: String? = null
) {
    fun isValidResponse(): Boolean = city != null && temperature != null && condition != null

    fun toRequest(city: String): FastApiWeatherModel {
        return this.copy(cityQuery = city)
    }
}