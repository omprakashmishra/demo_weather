package com.omslab.weather.data.repository

import com.omslab.weather.data.datasource.FactCheckDS
import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FactCheckModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactCheckRepository @Inject constructor(
    private val dataSource: FactCheckDS
) {

    suspend fun factCheck(claim: String): ApiResult<FactCheckModel> = dataSource.factCheck(FactCheckModel(claim = claim))

}