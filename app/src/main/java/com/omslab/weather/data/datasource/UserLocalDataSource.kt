package com.omslab.weather.data.datasource

import com.omslab.weather.data.mapper.UserMapper
import com.omslab.weather.data.dbcall.local.QueryDAO
import com.omslab.weather.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dao: QueryDAO
) {

    suspend fun registerUser(user: User): Result<Long> {
        return try {
            val userModel = UserMapper.mapToData(user)
            val result = dao.register(userModel)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUser(email: String, password: String): Flow<List<User>> {
        return dao.getUser(email, password).map { models ->
            UserMapper.mapToDomainList(models)
        }
    }

    suspend fun clearDatabase() {
         dao.clearDb()
    }

    suspend fun deleteOldLocations(email: String?) {
        dao.deleteOldLocations(email)
    }

}