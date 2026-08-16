package com.omslab.weather.domain.usecase.user

import com.omslab.weather.domain.models.User
import com.omslab.weather.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    suspend fun registerUser(user: User): Result<Long> {
        return repository.registerUser(user)
    }

    fun loginUser(
        email: String,
        password: String
    ): Flow<List<User>> {
        return repository.getUser(email, password)
    }

    suspend fun clearDatabase() {
        repository.clearDatabase()
    }

    suspend fun deleteOldLocations(email: String) {
        repository.deleteOldLocations(email)
    }
}