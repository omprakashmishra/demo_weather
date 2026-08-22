package com.omslab.weather.presentation.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.omslab.weather.R
import com.omslab.weather.databinding.FragmentCurrentWeatherBinding
import com.omslab.weather.presentation.viewmodel.DashboardViewModel
import com.omslab.weather.common.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentWeatherFm : Fragment() {

    private var binding: FragmentCurrentWeatherBinding? = null
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        showWelcomeMessage()
    }

    private fun saveLocation() {
        binding?.container?.ivAction?.setImageResource(R.drawable.outline_add_location)
        binding?.container?.ivAction?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveLocation()
            }
            Toast.makeText(activity, "Location saved successfully", Toast.LENGTH_LONG).show()
        }
        binding?.container?.ivAction?.visibility = View.VISIBLE
    }

    private fun showWelcomeMessage() {
        val email = viewModel.sharedPref.getString(Constants.PrimaryEmail)
        Toast.makeText(activity, "WELCOME - $email", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    private fun bindObservers() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                binding?.container?.apply {
                    saveLocation()
                    if (it.weatherIcon.contains("d")) {
                        ivWeatherSun.setImageResource(R.drawable.sunshine)
                    } else {
                        ivWeatherSun.setImageResource(R.drawable.night_ic)
                    }
                    tvTemperature.text = "Temperature: ${it.temperature}°C"
                    tvDescription.text = "Description: ${it.weatherDescription}"
                    tvCountry.text = "Country: ${it.country}"
                    tvCity.text = "City: ${it.cityName}"
                    tvSunset.text = "Sunset: ${viewModel.utcFormatted(it.sunset, Constants.timeAm)}"
                    tvSunrise.text = "Sunrise: ${viewModel.utcFormatted(it.sunrise, Constants.timeAm)}"
                }
            }
        }

        viewModel.weatherState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DashboardViewModel.WeatherState.Error -> {
                    Toast.makeText(activity, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    companion object {
        private var ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pos: Int) = CurrentWeatherFm().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, pos)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}