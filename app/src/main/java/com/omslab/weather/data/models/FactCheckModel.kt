package com.omslab.weather.data.models

import com.google.gson.annotations.SerializedName

/**
 * Unified model for the Fact Check feature.
 * Serves as: request body, response DTO, and UI result holder.
 */
data class FactCheckModel(
    @SerializedName("claim")       val claim: String = "",
    @SerializedName("verdict")     val verdict: String = "",
    @SerializedName("confidence")  val confidence: Int = 0,
    @SerializedName("explanation") val explanation: String = "",
    @SerializedName("sources")     val sources: List<Source> = emptyList()
) {
    /** True when the model holds an actual result (not just an input claim). */
    val hasResult: Boolean get() = verdict.isNotBlank()

    companion object {
        fun unverifiable(claim: String, reason: String) = FactCheckModel(
            claim = claim,
            verdict = "UNVERIFIABLE",
            confidence = 0,
            explanation = reason,
            sources = emptyList()
        )
    }

}

data class Source(
    @SerializedName("name") val name: String = "",
    @SerializedName("url")  val url: String = ""
)