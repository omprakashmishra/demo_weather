package com.omslab.weather.presentation.fastApiLlm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FactCheckModel
import com.omslab.weather.domain.usecase.location.FactCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FastApiWeatherViewModel @Inject constructor(
    private val factCheckUseCase: FactCheckUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(FactCheckUiState())
    val uiState: LiveData<FactCheckUiState> = _uiState

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    // ---------------- Input ----------------

    fun onInputChanged(text: String) = update { copy(input = text, error = null) }

    fun onMicToggled() = update { copy(isListening = !isListening) }

    fun onVoiceResult(text: String) = update {
        copy(input = text, isListening = false, error = null)
    }

    fun onVoiceError(message: String) = update {
        copy(isListening = false, error = message)
    }

    // ---------------- Action ----------------

    fun onSendClicked() {
        val state = _uiState.value ?: return
        val query = state.input.trim()

        when {
            query.isBlank() -> update { copy(error = "Please enter a claim.") }
            state.isLoading -> Unit
            else -> submit(query)
        }
    }

    private fun submit(query: String) {
        update {
            copy(
                isLoading = true,
                error = null,
                result = FactCheckModel()
            )
        }

        viewModelScope.launch {
            val newResult = when (val result = factCheckUseCase(query)) {
                is ApiResult.Success -> result.data.copy(
                    claim = result.data.claim.ifBlank { query },
                    confidence = result.data.confidence.coerceIn(0, 100)
                )
                is ApiResult.Error -> FactCheckModel.unverifiable(
                    claim = query,
                    reason = result.message
                )
                ApiResult.NetworkError -> FactCheckModel.unverifiable(
                    claim = query,
                    reason = "No internet connection."
                )
                ApiResult.Timeout -> FactCheckModel.unverifiable(
                    claim = query,
                    reason = "Request timed out. Please try again."
                )
            }

            update { copy(isLoading = false, result = newResult) }

            if (newResult.hasResult) _events.tryEmit(Event.ClearInput)
        }
    }

    // ---------------- Helpers ----------------

    private inline fun update(block: FactCheckUiState.() -> FactCheckUiState) {
        _uiState.value = _uiState.value?.block() ?: FactCheckUiState().block()
    }

    sealed interface Event {
        data object ClearInput : Event
    }
}