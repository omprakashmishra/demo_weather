package com.omslab.weather.domain.usecase.location

import com.omslab.weather.domain.models.Location
import com.omslab.weather.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val repository: ILocationRepository
) {

    suspend fun saveLocation(location: Location): Boolean {
        repository.saveLocation(location)
        return true
    }

    suspend fun getCurrentLocation(): Location? {
        return repository.getCurrentLocation()
    }

    fun getStoredLocations(email: String): Flow<List<Location>> {
        return repository.getStoredLocations(email)
    }

    suspend fun updateLocation(location: Location) {
        repository.updateLocation(location)
    }

    suspend fun deleteOldLocations(email: String) {
        repository.deleteOldLocations(email)
    }

    suspend fun deleteListLocation(id: Int) {
        repository.deleteListLocation(id)
    }
}