package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.LocationLocalDataSource
import com.omslab.weather.domain.models.Location
import com.omslab.weather.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val localDataSource: LocationLocalDataSource
) : ILocationRepository {

    override suspend fun saveLocation(location: Location) {
        localDataSource.saveLocation(location)
    }

    override suspend fun getCurrentLocation(): Location? {
        return localDataSource.getCurrentLocation()
    }

    override fun getStoredLocations(email: String): Flow<List<Location>> {
        return localDataSource.getStoredLocations(email)
    }

    override suspend fun updateLocation(location: Location) {
        localDataSource.updateLocation(location)
    }

    override suspend fun deleteOldLocations(email: String) {
        localDataSource.deleteOldLocations(email)
    }
}