package com.omslab.weather.presentation.fastApiLlm

import com.omslab.weather.data.models.FactCheckModel

data class FactCheckUiState(
    val input: String = "",
    val isLoading: Boolean = false,
    val isListening: Boolean = false,
    val result: FactCheckModel = FactCheckModel(),
    val error: String? = null,
    val retrySeconds: Int = 0
) {
    /** Send button should be enabled only when there is text and we're idle. */
    val canSend: Boolean get() = input.isNotBlank() && !isLoading

    /** True once a real verdict has come back. */
    val hasResult: Boolean get() = result.hasResult
}