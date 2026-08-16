package com.omslab.weather.data.mapper

import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.domain.models.Location

object LocationMapper {
    fun mapToDomain(model: UserLocationTableModel): Location {
        return Location(
            lat = model.lat,
            lon = model.lon,
            cityName = model.cityName,
            country = model.country,
            temperature = model.temperature,
            description = model.description,
            icon = model.icon,
            sunrise = model.sunrise,
            sunset = model.sunset,
            entryDateTime = model.entryDateTime
        )
    }

    fun mapToData(location: Location, email: String): UserLocationTableModel {
        return UserLocationTableModel(
            lat = location.lat,
            lon = location.lon,
            cityName = location.cityName ?: "",
            country = location.country ?: "",
            temperature = location.temperature ?: "",
            description = location.description ?: "",
            icon = location.icon ?: "",
            sunrise = location.sunrise ?: "",
            sunset = location.sunset ?: "",
            entryDateTime = location.entryDateTime ?: "",
            email = email
        )
    }
}