package com.omslab.weather.data.repository

import com.omslab.weather.data.dbcall.remoteQuery.FactCheckApi
import com.omslab.weather.data.models.FactCheckModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactCheckRepository @Inject constructor(
    private val api: FactCheckApi
) {
    suspend fun factCheck(claim: String): FactCheckModel =
        api.factCheck(FactCheckModel(claim = claim))
}