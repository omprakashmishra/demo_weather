package com.omslab.weather.domain.repository

import com.omslab.weather.domain.models.Location
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    suspend fun saveLocation(location: Location)
    suspend fun getCurrentLocation(): Location?
    fun getStoredLocations(email: String): Flow<List<Location>>
    suspend fun updateLocation(location: Location)
}