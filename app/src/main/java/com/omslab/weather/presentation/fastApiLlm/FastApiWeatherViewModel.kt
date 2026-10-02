package com.omslab.weather.presentation.fastApiLlm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FactCheckModel
import com.omslab.weather.domain.usecase.location.FactCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var retryJob: Job? = null

    // ---------------- Input ----------------

    fun onInputChanged(text: String) =
        update {
            copy(
                input = text,
                error = null
            )
        }

    fun onMicToggled() =
        update {
            copy(isListening = !isListening)
        }

    fun onVoiceResult(text: String) =
        update { copy(input = text, isListening = false, error = null)
        }

    fun onVoiceError(message: String) =
        update {
            copy(
                isListening = false,
                error = message
            )
        }

    // ---------------- Action ----------------

    fun onSendClicked() {
        val state = _uiState.value ?: return
        val query = state.input.trim()

        when {
            query.isBlank() -> {
                update {
                    copy(error = "Please enter a claim.")
                }
            }

            state.isLoading -> Unit

            state.retrySeconds > 0 -> Unit

            else -> submit(query)
        }
    }

    private fun submit(query: String) {
        retryJob?.cancel()
        update { copy(isLoading = true, error = null, retrySeconds = 0, result = FactCheckModel()) }

        viewModelScope.launch {

            val newResult = when (val result = factCheckUseCase(query)) {

                is ApiResult.Success -> {
                    result.data.copy(claim = result.data.claim.ifBlank { query }, confidence = result.data.confidence.coerceIn(0, 100))
                }

                is ApiResult.Error -> {
                    if (result.message.contains("429") || result.message.contains("RESOURCE_EXHAUSTED")) {
                        FactCheckModel.unverifiable(claim = query, reason = "API quota exceeded. Please wait before trying again.")
                    } else {
                        FactCheckModel.unverifiable(claim = query,reason = result.message)
                    }
                }
                ApiResult.NetworkError -> {
                    FactCheckModel.unverifiable(claim = query, reason = "No internet connection.")
                }
                ApiResult.Timeout -> {
                    FactCheckModel.unverifiable(claim = query, reason = "Request timed out. Please try again.")
                }
            }
            update { copy(isLoading = false, result = newResult) }
            if (newResult.hasResult) {
                _events.tryEmit(Event.ClearInput)
            }
        }
    }


    // ---------------- Helpers ----------------

    private inline fun update(block: FactCheckUiState.() -> FactCheckUiState) {
        _uiState.value = _uiState.value?.block() ?: FactCheckUiState().block()
    }

    override fun onCleared() {
        retryJob?.cancel()
        super.onCleared()
    }

    sealed interface Event {
        data object ClearInput : Event
    }
}