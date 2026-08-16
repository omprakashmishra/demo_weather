package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.UserLocalDataSource
import com.omslab.weather.domain.models.User
import com.omslab.weather.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource
) : IUserRepository {

    override suspend fun registerUser(user: User): Result<Long> {
        return localDataSource.registerUser(user)
    }

    override fun getUser(email: String, password: String): Flow<List<User>> {
        return localDataSource.getUser(email, password)
    }

    override suspend fun clearDatabase() {
        return localDataSource.clearDatabase()
    }

    override suspend fun deleteOldLocations(email: String) {
        return localDataSource.deleteOldLocations(email)
    }

}