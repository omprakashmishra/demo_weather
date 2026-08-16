package com.omslab.weather.data.datasource

import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.mapper.LocationMapper
import com.omslab.weather.data.dbcall.local.QueryDAO
import com.omslab.weather.domain.models.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationLocalDataSource @Inject constructor(
    private val dao: QueryDAO,
    private val sharedPref: MySharedPreference
) {

    suspend fun deleteOldLocations(email: String) {
        dao.deleteOldLocations(email)
    }
    suspend fun saveLocation(location: Location) {
        sharedPref.setString(Constants.UpdatedLat, location.lat)
        sharedPref.setString(Constants.UpdatedLong, location.lon)

        val email = sharedPref.getString(Constants.PrimaryEmail) ?: ""
        val locationModel = LocationMapper.mapToData(location, email)
        dao.insertLocationData(locationModel)
    }

     fun getCurrentLocation(): Location? {
        val lat = sharedPref.getString(Constants.UpdatedLat)
        val lon = sharedPref.getString(Constants.UpdatedLong)
        return if (!lat.isNullOrEmpty() && !lon.isNullOrEmpty()) {
            Location(lat = lat, lon = lon)
        } else null
    }

    fun getStoredLocations(email: String): Flow<List<Location>> {
        return dao.getStoredLocation(email).map { models ->
            models.map { LocationMapper.mapToDomain(it) }
        }
    }

    suspend fun updateLocation(location: Location) {
        sharedPref.setString(Constants.UpdatedLat, location.lat)
        sharedPref.setString(Constants.UpdatedLong, location.lon)

        val email = sharedPref.getString(Constants.PrimaryEmail) ?: ""
        val locationModel = LocationMapper.mapToData(location, email)
        dao.insertLocationData(locationModel)
    }
}