package com.omslab.weather.presentation.fastApiLlm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.data.dbcall.networkBase.ApiResult
import com.omslab.weather.data.models.FastApiWeatherModel
import com.omslab.weather.domain.usecase.location.GetWeatherFastApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FastApiWeatherViewModel @Inject constructor(
    private val getWeatherFastApiUseCase: GetWeatherFastApiUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun getWeather(city: String) {

        val request = FastApiWeatherModel(
            city = city
        )

        _state.value = State.Loading

        viewModelScope.launch {

            when (val result = getWeatherFastApiUseCase(request)) {

                is ApiResult.Success -> {
                    _state.value = State.Success(
                        request = request,
                        data = result.data
                    )
                }

                is ApiResult.Error -> {
                    _state.value = State.Error(
                        result.message
                    )
                }

                ApiResult.NetworkError -> {
                    _state.value = State.Error(
                        "No internet connection."
                    )
                }

                ApiResult.Timeout -> {
                    _state.value = State.Error(
                        "Request timeout."
                    )
                }
            }
        }
    }

    sealed class State {

        object Loading : State()

        data class Success(
            val request: FastApiWeatherModel,
            val data: FastApiWeatherModel
        ) : State()

        data class Error(
            val message: String
        ) : State()
    }
}