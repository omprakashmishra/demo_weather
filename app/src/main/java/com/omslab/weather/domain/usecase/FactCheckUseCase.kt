package com.omslab.weather.domain.usecase.location

import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FactCheckModel
import com.omslab.weather.data.repository.FactCheckRepository
import javax.inject.Inject

class FactCheckUseCase @Inject constructor(
    private val repository: FactCheckRepository
) {
    suspend operator fun invoke(claim: String): ApiResult<FactCheckModel> =
        repository.factCheck(claim)
}