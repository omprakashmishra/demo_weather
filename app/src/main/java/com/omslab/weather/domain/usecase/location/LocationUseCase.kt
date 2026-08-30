package com.omslab.weather.domain.usecase.location

import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.data.repository.LocationRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val repository: LocationRepositoryImpl
) {
    suspend fun saveLocation(location: UserLocationTableModel): Boolean {
        repository.saveLocation(location)
        return true
    }

    suspend fun getCurrentLocation(): UserLocationTableModel? = repository.getCurrentLocation()

    fun getStoredLocations(email: String): Flow<List<UserLocationTableModel>> =
        repository.getStoredLocations(email)

    suspend fun updateLocation(location: UserLocationTableModel) {
        repository.updateLocation(location)
    }

    suspend fun deleteOldLocations(email: String) {
        repository.deleteOldLocations(email)
    }

    suspend fun deleteListLocation(id: Int) {
        repository.deleteListLocation(id)
    }
}
