package com.omslab.weather.domain.usecase.fastApi
import com.omslab.weather.data.models.FactCheckModel
import com.omslab.weather.data.repository.FactCheckRepository
import javax.inject.Inject

class GetWeatherFastApiUseCase @Inject constructor(
    private val repository: FactCheckRepository
) {
    suspend operator fun invoke(claim: String): FactCheckModel =
        repository.factCheck(claim)
}