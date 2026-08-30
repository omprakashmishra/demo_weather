package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.UserLocalDataSource
import com.omslab.weather.data.models.TableModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource
) {

    suspend fun registerUser(user: TableModel): Result<Long> {
        return localDataSource.registerUser(user)
    }

    fun getUser(email: String, password: String): Flow<List<TableModel>> {
        return localDataSource.getUser(email, password)
    }

    suspend fun clearDatabase() {
        return localDataSource.clearDatabase()
    }

    suspend fun deleteOldLocations(email: String) {
        return localDataSource.deleteOldLocations(email)
    }

}