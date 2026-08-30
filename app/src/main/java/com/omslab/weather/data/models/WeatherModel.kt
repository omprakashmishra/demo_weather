// WeatherModels.kt
package com.omslab.weather.data.models

import com.google.gson.annotations.SerializedName

/**
 * Unified Weather Model - Handles both request and response
 */
data class WeatherModel(
    // Response fields
    @SerializedName("base")
    val base: String? = null,

    @SerializedName("clouds")
    val clouds: CloudsModel? = null,

    @SerializedName("cod")
    val cod: Int? = null,

    @SerializedName("coord")
    val coord: CoordModel? = null,

    @SerializedName("dt")
    val dt: Long? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("main")
    val main: MainModel? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("snow")
    val snow: SnowModel? = null,

    @SerializedName("sys")
    val sys: SysModel? = null,

    @SerializedName("timezone")
    val timezone: Int? = null,

    @SerializedName("visibility")
    val visibility: Int? = null,

    @SerializedName("weather")
    val weather: List<WeatherConditionModel>? = null,

    @SerializedName("wind")
    val wind: WindModel? = null,

    // Request fields (for building requests)
    @SerializedName("units")
    val units: String? = null,

    @SerializedName("lat")
    val lat: String? = null,

    @SerializedName("lon")
    val lon: String? = null,

    @SerializedName("appid")
    val appId: String? = null,

    @SerializedName("city")
    val city: String? = null, // For FastAPI

    @SerializedName("temperature")
    val apiTemperature: Int? = null,

    @SerializedName("condition")
    val condition: String? = null
) {
    // App-facing values used directly by presentation/domain use cases.
    val cityName: String get() = name ?: city.orEmpty()
    val temperature: Double get() = main?.temp ?: apiTemperature?.toDouble() ?: 0.0
    val feelsLike: Double get() = main?.feelsLike ?: 0.0
    val humidityValue: Int get() = main?.humidity ?: 0
    val pressureValue: Int get() = main?.pressure ?: 0
    val windSpeed: Double get() = wind?.speed ?: 0.0
    val weatherDescription: String get() = weather?.firstOrNull()?.description ?: condition.orEmpty()
    val weatherIcon: String get() = weather?.firstOrNull()?.icon ?: ""
    val latitude: Double get() = coord?.lat ?: lat?.toDoubleOrNull() ?: 0.0
    val longitude: Double get() = coord?.lon ?: lon?.toDoubleOrNull() ?: 0.0
    val country: String get() = sys?.country.orEmpty()
    val sunrise: Long get() = sys?.sunrise ?: 0L
    val sunset: Long get() = sys?.sunset ?: 0L
    val timestamp: Long get() = dt ?: 0L

    // Helper method to check if response is valid
    fun isValidResponse(): Boolean =
        (id != null && name != null && main != null) ||
        (city != null && apiTemperature != null && condition != null)

    // Helper method to create request
    fun toRequest(units: String, lat: String, lon: String, appId: String): WeatherModel {
        return this.copy(
            units = units,
            lat = lat,
            lon = lon,
            appId = appId
        )
    }
}

// Nested models
data class CloudsModel(
    @SerializedName("all")
    val all: Int? = null
)

data class CoordModel(
    @SerializedName("lat")
    val lat: Double? = null,

    @SerializedName("lon")
    val lon: Double? = null
)

data class MainModel(
    @SerializedName("feels_like")
    val feelsLike: Double? = null,

    @SerializedName("grnd_level")
    val groundLevel: Int? = null,

    @SerializedName("humidity")
    val humidity: Int? = null,

    @SerializedName("pressure")
    val pressure: Int? = null,

    @SerializedName("sea_level")
    val seaLevel: Int? = null,

    @SerializedName("temp")
    val temp: Double? = null,

    @SerializedName("temp_max")
    val tempMax: Double? = null,

    @SerializedName("temp_min")
    val tempMin: Double? = null
)

data class SnowModel(
    @SerializedName("1h")
    val oneHour: Double? = null
)

data class SysModel(
    @SerializedName("country")
    val country: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("sunrise")
    val sunrise: Long? = null,

    @SerializedName("sunset")
    val sunset: Long? = null,

    @SerializedName("type")
    val type: Int? = null
)

data class WeatherConditionModel(
    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("main")
    val main: String? = null
)

data class WindModel(
    @SerializedName("deg")
    val deg: Int? = null,

    @SerializedName("gust")
    val gust: Double? = null,

    @SerializedName("speed")
    val speed: Double? = null
)