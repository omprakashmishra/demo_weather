package com.omslab.weather.domain.repository

import com.omslab.weather.domain.models.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun registerUser(user: User): Result<Long>
    fun getUser(email: String, password: String): Flow<List<User>>

    suspend fun clearDatabase()

    suspend fun deleteOldLocations(email: String)
}