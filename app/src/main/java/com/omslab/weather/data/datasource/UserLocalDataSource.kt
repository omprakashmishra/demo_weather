package com.omslab.weather.data.datasource

import com.omslab.weather.data.dbcall.local.QueryDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dao: QueryDAO
) {

    suspend fun registerUser(user: com.omslab.weather.data.models.TableModel): Result<Long> {
        return try {
            val result = dao.register(user)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUser(email: String, password: String): Flow<List<com.omslab.weather.data.models.TableModel>> {
        return dao.getUser(email, password).map { models ->
            models
        }
    }

    suspend fun clearDatabase() {
         dao.clearDb()
    }

    suspend fun deleteOldLocations(email: String?) {
        dao.deleteOldLocations(email)
    }

}