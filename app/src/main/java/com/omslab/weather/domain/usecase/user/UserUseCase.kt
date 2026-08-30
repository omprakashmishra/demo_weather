package com.omslab.weather.domain.usecase.user

import com.omslab.weather.data.models.TableModel
import com.omslab.weather.data.repository.UserRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
) {
    suspend fun registerUser(user: TableModel): Result<Long> = repository.registerUser(user)

    fun loginUser(email: String, password: String): Flow<List<TableModel>> =
        repository.getUser(email, password)

    suspend fun clearDatabase() {
        repository.clearDatabase()
    }

    suspend fun deleteOldLocations(email: String) {
        repository.deleteOldLocations(email)
    }
}
