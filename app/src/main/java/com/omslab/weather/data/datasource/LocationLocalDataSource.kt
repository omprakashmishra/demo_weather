package com.omslab.weather.data.datasource

import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.dbcall.local.QueryDAO
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

    suspend fun deleteLocationById(id: Int) {
        dao.deleteLocationById(id)
    }
    suspend fun saveLocation(location: com.omslab.weather.data.models.UserLocationTableModel) {
        sharedPref.setString(Constants.UpdatedLat, location.lat)
        sharedPref.setString(Constants.UpdatedLong, location.lon)

        val email = sharedPref.getString(Constants.PrimaryEmail) ?: ""
        location.email = email
        dao.insertLocationData(location)
    }

     fun getCurrentLocation(): com.omslab.weather.data.models.UserLocationTableModel? {
        val lat = sharedPref.getString(Constants.UpdatedLat)
        val lon = sharedPref.getString(Constants.UpdatedLong)
        return if (!lat.isNullOrEmpty() && !lon.isNullOrEmpty()) {
            com.omslab.weather.data.models.UserLocationTableModel(lat = lat, lon = lon)
        } else null
    }

    fun getStoredLocations(email: String): Flow<List<com.omslab.weather.data.models.UserLocationTableModel>> {
        return dao.getStoredLocation(email)
    }

    suspend fun updateLocation(location: com.omslab.weather.data.models.UserLocationTableModel) {
        sharedPref.setString(Constants.UpdatedLat, location.lat)
        sharedPref.setString(Constants.UpdatedLong, location.lon)

        val email = sharedPref.getString(Constants.PrimaryEmail) ?: ""
        location.email = email
        dao.insertLocationData(location)
    }
}