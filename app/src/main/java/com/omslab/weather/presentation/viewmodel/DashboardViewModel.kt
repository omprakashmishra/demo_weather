package com.collabera.weather.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collabera.weather.common.MySharedPreference
import com.collabera.weather.models.UserLocationTableModel
import com.collabera.weather.models.WeatherDataModel
import com.collabera.weather.repo.DBRepository
import com.collabera.weather.repo.WeatherRepository
import com.collabera.weather.common.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    val sp: MySharedPreference,
    private val networkRepo: WeatherRepository,
    private val dbRepository: DBRepository
) : ViewModel() {

    private val _response = MutableLiveData<WeatherDataModel>()
    val weatherResponse: LiveData<WeatherDataModel>
        get() = _response

    init {
        // Check if we have stored location data
        val lat = sp.getString(Constants.UpdatedLat)
        val long = sp.getString(Constants.UpdatedLong)

        // Only fetch weather if both lat and long are available
        if (!lat.isNullOrEmpty() && !long.isNullOrEmpty()) {
            updateWeatherBasedOnLatestLatLong(lat, long, Constants.AppId)
        } else {
            Log.d("===>", "No stored location found. Waiting for location update.")
        }

        getStoredLocation(sp.getString(Constants.PrimaryEmail)!!)
    }

    fun updateWeatherBasedOnLatestLatLong(lat: String, long: String, appId: String) = viewModelScope.launch {
        // Add logging to verify values
        Log.d("===>", "Fetching weather for Lat: $lat, Long: $long")

        if (lat.isEmpty() || long.isEmpty()) {
            Log.e("===>", "Latitude or Longitude is empty!")
            return@launch
        }

        networkRepo.getWeatherByLocation(lat, long, appId).let { response ->
            if (response.isSuccessful) {
                Log.d("===>getWeatherAPI", "response: ${response.body()}")
                _response.postValue(response.body())
            } else {
                Log.e("===>", "Error Type Code: ${response.code()}, Message: ${response.message()}")
                Log.e("===>", "Error Body: ${response.errorBody()?.string()}")
            }
        }
    }

    fun enterUserLocation(locationData: UserLocationTableModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.insertLocationData(locationData)
        }
    }

    private val _weatherList = MutableLiveData<List<UserLocationTableModel>>()
    val weatherList: LiveData<List<UserLocationTableModel>> get() = _weatherList

    private fun getStoredLocation(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.getStoredLocation(email).collect { item ->
                item.let {
                    _weatherList.postValue(item)
                }
            }
        }
    }

    //------------------------------------------------
    fun utcFormatted(time: Long, tmPattern: String): String? {
        return SimpleDateFormat(tmPattern, Locale.ENGLISH).format(Date(time * 1000))
    }

    fun storeLatLong(lat: String, long: String) {
        // Get stored values
        val storedLat = sp.getString(Constants.UpdatedLat)
        val storedLong = sp.getString(Constants.UpdatedLong)

        // Only update if coordinates have changed
        if (lat == storedLat && long == storedLong) {
            Log.d("===>", "Location unchanged: $lat|$long")
            return
        }

        // Store new coordinates
        sp.setString(Constants.UpdatedLat, lat)
        sp.setString(Constants.UpdatedLong, long)

        Log.d("===>", "Storing new location: Lat: $lat, Long: $long")

        // Fetch weather for new location
        updateWeatherBasedOnLatestLatLong(lat, long, Constants.AppId)
    }
}