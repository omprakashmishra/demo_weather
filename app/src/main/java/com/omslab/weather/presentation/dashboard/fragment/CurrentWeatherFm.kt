package com.omslab.weather.presentation.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.omslab.weather.R
import com.omslab.weather.common.util.Constants
import com.omslab.weather.databinding.FragmentCurrentWeatherBinding
import com.omslab.weather.presentation.dashboard.DashboardViewModel
import com.omslab.weather.presentation.fastApiLlm.FastApiWeatherActivity
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
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        bindObservers()
        showWelcomeMessage()
    }

    // ---------------- Click listeners ----------------

    private fun setupClickListeners() {
        with(binding ?: return) {
            // Save-location button
            container.ivAction.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.outline_add_location)
                setOnClickListener {
                    lifecycleScope.launch { viewModel.saveLocation() }
                    toast("Location saved successfully")
                }
            }

            // Tap the card → open Fact Check screen
            container.root.setOnClickListener { openFactCheck() }
        }
    }

    private fun openFactCheck() {
        val weather = viewModel.currentWeather.value ?: run {
            toast("Weather data not ready yet")
            return
        }
        startActivity(
            Intent(requireContext(), FastApiWeatherActivity::class.java).apply {
                putExtra("lat", weather.latitude)
                putExtra("lon", weather.longitude)
                putExtra("city", weather.cityName)
            }
        )
    }

    // ---------------- Observers ----------------

    @SuppressLint("SetTextI18n")
    private fun bindObservers() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            weather ?: return@observe
            binding?.container?.apply {
                if (weather.weatherIcon.contains("d")) {
                    ivWeatherSun.setImageResource(R.drawable.sunshine)
                } else {
                    ivWeatherSun.setImageResource(R.drawable.night_ic)
                }
                tvTemperature.text = "Temperature: ${weather.temperature}°C"
                tvDescription.text = "Description: ${weather.weatherDescription}"
                tvCountry.text = "Country: ${weather.country}"
                tvCity.text = "City: ${weather.cityName}"
                tvSunset.text = "Sunset: ${viewModel.utcFormatted(weather.sunset, Constants.timeAm)}"
                tvSunrise.text = "Sunrise: ${viewModel.utcFormatted(weather.sunrise, Constants.timeAm)}"
            }
        }

        viewModel.weatherState.observe(viewLifecycleOwner) { state ->
            if (state is DashboardViewModel.WeatherState.Error) {
                toast("Error: ${state.message}")
            }
        }
    }

    private fun showWelcomeMessage() {
        val email = viewModel.sharedPref.getString(Constants.PrimaryEmail)
        toast("WELCOME - $email")
    }

    // ---------------- Helpers ----------------

    private fun toast(msg: String) {
        activity?.let { Toast.makeText(it, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pos: Int) = CurrentWeatherFm().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, pos)
            }
        }
    }
}