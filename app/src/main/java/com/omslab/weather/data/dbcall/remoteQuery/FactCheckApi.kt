package com.omslab.weather.data.dbcall.remoteQuery

import com.omslab.weather.data.models.FactCheckModel
import retrofit2.http.Body
import retrofit2.http.POST

interface FactCheckApi {
    @POST("fact-check")
    suspend fun factCheck(@Body request: FactCheckModel): FactCheckModel
}