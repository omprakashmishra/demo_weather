package com.omslab.weather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.domain.models.Location
import com.omslab.weather.domain.models.Weather
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

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> get() = _weatherState

    private val _locationState = MutableLiveData<LocationState>()
    val locationState: LiveData<LocationState> get() = _locationState

    private val _weatherList = MutableLiveData<List<Location>>()
    val weatherList: LiveData<List<Location>> get() = _weatherList

    private val _currentWeather = MutableLiveData<Weather?>()
    val currentWeather: LiveData<Weather?> get() = _currentWeather

    init {
        loadStoredLocations()
    }

    private fun loadStoredLocations() {
        val email = sharedPref.getString(Constants.PrimaryEmail) ?: return

        viewModelScope.launch {
            locationUseCase.getStoredLocations(email)
                .catch {
                    _locationState.postValue(
                        LocationState.Error("Failed to load locations")
                    )
                }
                .collectLatest { locations ->
                    _weatherList.postValue(locations)
                    if (locations.isEmpty()) {
                        loadLocationFromPreferences()
                    }else{
                        val firstLocation = locations.firstOrNull()
                        firstLocation?.let {
                            fetchWeather(it.lat, it.lon)
                        }
                    }
                }
        }
    }

    private fun loadLocationFromPreferences() {
        viewModelScope.launch {
            val location = locationUseCase.getCurrentLocation()
            location?.let {
                if (it.lat.isNotEmpty() && it.lon.isNotEmpty()) {
                    fetchWeather(it.lat, it.lon)
                }
            }
        }
    }

    fun fetchWeather(lat: String, lon: String) {
        if (lat.isEmpty() || lon.isEmpty()) {
            _weatherState.postValue(WeatherState.Error("Latitude or Longitude is empty"))
            return
        }

        _weatherState.postValue(WeatherState.Loading)

        viewModelScope.launch {
            val result = getWeatherUseCase(lat, lon)
            result.fold(
                onSuccess = { weather ->
                    _currentWeather.postValue(weather)
                    _weatherState.postValue(WeatherState.Success(weather))

                    val location = Location(
                        lat = lat,
                        lon = lon,
                        cityName = weather.cityName,
                        country = weather.country,
                        temperature = weather.temperature.toString(),
                        description = weather.weatherDescription,
                        icon = weather.weatherIcon,
                        sunrise = utcFormatted(weather.sunrise, Constants.timeAm),
                        sunset = utcFormatted(weather.sunset, Constants.timeAm),
                        entryDateTime = utcFormatted(weather.timestamp, Constants.dateTimeAm)
                    )
                    saveLocation(location)
                },
                onFailure = { error ->
                    _weatherState.postValue(WeatherState.Error(error.message ?: "Unknown error occurred"))
                }
            )
        }
    }

    fun saveLocation(location: Location) {
        viewModelScope.launch {
            try {
                val saved = locationUseCase.saveLocation(location)
                if (!saved) {
                    _locationState.postValue(LocationState.Error("Location already exists"))
                }
            } catch (e: Exception) {
                _locationState.postValue(
                    LocationState.Error("Failed to save location")
                )
            }
        }
    }

    fun deleteOldLocations() {
        viewModelScope.launch {
            try {
                val email = sharedPref.getString(Constants.PrimaryEmail)
                locationUseCase.deleteOldLocations(email ?: "")
            } catch (e: Exception) {
                _locationState.postValue(LocationState.Error("Failed to delete old locations"))
            }
        }
    }



    fun storeLatLong(lat: String, lon: String) {
        val storedLat = sharedPref.getString(Constants.UpdatedLat)
        val storedLon = sharedPref.getString(Constants.UpdatedLong)

        if (lat == storedLat && lon == storedLon) {
            return
        }
        fetchWeather(lat, lon)
         sharedPref.setString(Constants.UpdatedLat, lat)
        sharedPref.setString(Constants.UpdatedLong, lon)
    }

    fun utcFormatted(time: Long, pattern: String): String? {
        return try {
            SimpleDateFormat(pattern, Locale.ENGLISH).format(Date(time * 1000))
        } catch (e: Exception) {
            null
        }
    }

    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val weather: Weather) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }

    sealed class LocationState {
        object Saved : LocationState()
        data class Error(val message: String) : LocationState()
    }
}