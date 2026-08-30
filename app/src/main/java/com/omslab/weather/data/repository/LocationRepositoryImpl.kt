package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.LocationLocalDataSource
import com.omslab.weather.data.models.UserLocationTableModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val localDataSource: LocationLocalDataSource
) {

    suspend fun saveLocation(location: UserLocationTableModel) {
        localDataSource.saveLocation(location)
    }

    suspend fun getCurrentLocation(): UserLocationTableModel? {
        return localDataSource.getCurrentLocation()
    }

    fun getStoredLocations(email: String): Flow<List<UserLocationTableModel>> {
        return localDataSource.getStoredLocations(email)
    }

    suspend fun updateLocation(location: UserLocationTableModel) {
        localDataSource.updateLocation(location)
    }

    suspend fun deleteOldLocations(email: String) {
        localDataSource.deleteOldLocations(email)
    }

    suspend fun deleteListLocation(id: Int) {
        // Implement the logic to delete a specific location by its ID
        // This method should be implemented in the local data source and called here
        // For example:
         localDataSource.deleteLocationById(id)
    }
}