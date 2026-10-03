package com.omslab.weather.domain.usecase.location

import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FactCheckModel
import com.omslab.weather.data.models.Source
import com.omslab.weather.data.repository.FactCheckRepository
import javax.inject.Inject

class FactCheckUseCase @Inject constructor(
    private val repository: FactCheckRepository
) {

    suspend operator fun invoke(claim: String): ApiResult<FactCheckModel> {
        // return mockData()
        return repository.factCheck(claim)
    }
    private fun mockData(): ApiResult<FactCheckModel> {

        val mockData = FactCheckModel(
            verdict = "FALSE",
            confidence = 95,
            explanation = "Light does not have a single 'exact' color, nor is it inherently white. Visible light is electromagnetic radiation composed of various wavelengths, each corresponding to a different perceived color. What we perceive as 'white light' is actually a polychromatic mixture of all wavelengths across the visible spectrum combined, not an intrinsic single color of light.",
            sources = listOf(
                Source(
                    name = "NASA Science - Visible Light",
                    url = "https://science.nasa.gov/ems/09_visiblelight"
                ),
                Source(
                    name = "Encyclopaedia Britannica - Colour",
                    url = "https://www.britannica.com/science/color"
                )
            )
        )

        return ApiResult.Success(mockData)
    }
}