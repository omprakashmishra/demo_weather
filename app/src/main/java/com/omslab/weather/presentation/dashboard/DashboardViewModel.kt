package com.omslab.weather.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.data.models.WeatherModel
import com.omslab.weather.domain.usecase.location.LocationUseCase
import com.omslab.weather.domain.usecase.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val sharedPref: MySharedPreference,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private var savedWeather: WeatherModel? = null

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> get() = _weatherState

    private val _locationState = MutableLiveData<LocationState>()
    val locationState: LiveData<LocationState> get() = _locationState

    private val _weatherList =
        MutableLiveData<List<UserLocationTableModel>>()

    val weatherList: LiveData<List<UserLocationTableModel>>
        get() = _weatherList

    private val _currentWeather =
        MutableLiveData<WeatherModel?>()

    val currentWeather: LiveData<WeatherModel?>
        get() = _currentWeather

    init {
        loadStoredLocations()
    }

    /**
     * Loads saved locations.
     *
     * IMPORTANT:
     * This does NOT automatically call the weather API.
     */
    private fun loadStoredLocations() {
        val email = sharedPref.getString(Constants.PrimaryEmail)
            ?: return
        viewModelScope.launch {
            locationUseCase.getStoredLocations(email)
                .catch {
                    _locationState.postValue(
                        LocationState.Error(
                            "Failed to load locations"
                        )
                    )
                }
                .collectLatest { locations ->

                    _weatherList.postValue(locations)

                    if (locations.isEmpty()) {
                        loadLocationFromPreferences()
                    }
                }
        }
    }

    /**
     * Gets current latitude/longitude.
     *
     * Does NOT automatically call weather API.
     */
    private fun loadLocationFromPreferences() {
        viewModelScope.launch {
            val location = locationUseCase.getCurrentLocation()
            location?.let {
                if (it.lat.isNotEmpty() && it.lon.isNotEmpty()) {
                    storeLatLongOnly(it.lat, it.lon)
                }
            }
        }
    }

    /**
     * Explicit weather API call.
     *
     * Call this only when Dashboard actually needs
     * fresh weather data.
     */
    fun fetchWeather(
        lat: String,
        lon: String
    ) {

        if (lat.isBlank() || lon.isBlank()) {
            _weatherState.postValue(
                WeatherState.Error(
                    "Latitude or Longitude is empty"
                )
            )
            return
        }

        _weatherState.postValue(
            WeatherState.Loading
        )

        viewModelScope.launch {

            val result = getWeatherUseCase(
                lat,
                lon
            )

            result.fold(

                onSuccess = { weather ->

                    savedWeather = weather

                    _currentWeather.postValue(
                        weather
                    )

                    _weatherState.postValue(
                        WeatherState.Success(weather)
                    )
                },

                onFailure = { error ->

                    _weatherState.postValue(
                        WeatherState.Error(
                            error.message
                                ?: "Unknown error occurred"
                        )
                    )
                }
            )
        }
    }

    /**
     * Store latitude/longitude only.
     *
     * No network call.
     */
    private fun storeLatLongOnly(
        lat: String,
        lon: String
    ) {
        sharedPref.setString(
            Constants.UpdatedLat,
            lat
        )

        sharedPref.setString(
            Constants.UpdatedLong,
            lon
        )
    }

    /**
     * Update latitude/longitude and fetch weather
     * only when coordinates actually change.
     */
    fun storeLatLong(lat: String, lon: String) {

        if (lat.isBlank() || lon.isBlank()) {
            return
        }

        val storedLat =
            sharedPref.getString(Constants.UpdatedLat)

        val storedLon =
            sharedPref.getString(Constants.UpdatedLong)

        if (lat == storedLat && lon == storedLon) {
            return
        }

        storeLatLongOnly(
            lat,
            lon
        )

        fetchWeather(
            lat,
            lon
        )
    }

    /**
     * Save currently loaded weather.
     */
    fun saveLocation() {

        val weather = savedWeather
            ?: return

        viewModelScope.launch {

            val location = UserLocationTableModel(

                lat = weather.latitude.toString(),

                lon = weather.longitude.toString(),

                cityName = weather.cityName,

                country = weather.country,

                temperature = weather.temperature.toString(),

                description = weather.weatherDescription,

                icon = weather.weatherIcon,

                sunrise = utcFormatted(
                    weather.sunrise,
                    Constants.timeAm
                ).orEmpty(),

                sunset = utcFormatted(
                    weather.sunset,
                    Constants.timeAm
                ).orEmpty(),

                entryDateTime = utcFormatted(
                    weather.timestamp,
                    Constants.dateTimeAm
                ).orEmpty()
            )

            sharedPref.setString(
                Constants.UpdatedLat,
                location.lat
            )

            sharedPref.setString(
                Constants.UpdatedLong,
                location.lon
            )

            locationUseCase.saveLocation(
                location
            )

            _weatherState.postValue(
                WeatherState.Success(weather)
            )
        }
    }

    fun deleteOldLocations() {
        viewModelScope.launch {
            try {
                val email = sharedPref.getString(
                        Constants.PrimaryEmail
                    ).orEmpty()
                locationUseCase.deleteOldLocations(email)
            } catch (_: Exception) {
                _locationState.postValue(
                    LocationState.Error(
                        "Failed to delete old locations"
                    )
                )
            }
        }
    }

    fun deleteListLocation(id: Int) {
        viewModelScope.launch {
            try {
                locationUseCase.deleteListLocation(id)
            } catch (_: Exception) {
                _locationState.postValue(LocationState.Error("Failed to delete location"))
            }
        }
    }

    fun utcFormatted(
        time: Long,
        pattern: String
    ): String? {
        return try {
            SimpleDateFormat(
                pattern,
                Locale.ENGLISH
            ).format(
                Date(time * 1000)
            )

        } catch (_: Exception) {

            null
        }
    }

    sealed class WeatherState {

        object Loading : WeatherState()

        data class Success(
            val weather: WeatherModel
        ) : WeatherState()

        data class Error(
            val message: String
        ) : WeatherState()
    }

    sealed class LocationState {

        object Saved : LocationState()

        data class Error(
            val message: String
        ) : LocationState()
    }
}