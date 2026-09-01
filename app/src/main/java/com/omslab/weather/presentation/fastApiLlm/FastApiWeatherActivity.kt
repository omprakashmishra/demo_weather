package com.omslab.weather.presentation.fastApiLlm

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.omslab.weather.R
import com.omslab.weather.databinding.FastApiWeatherAcBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class FastApiWeatherActivity : AppCompatActivity() {

    private lateinit var binding: FastApiWeatherAcBinding

    private val viewModel: FastApiWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FastApiWeatherAcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()

            viewModel.getWeather(city)
        }

        observeWeather()
    }

    private fun observeWeather() {
        viewModel.state.observe(this) { state ->

            when (state) {

                FastApiWeatherViewModel.State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is FastApiWeatherViewModel.State.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val response = state.data

                    binding.tvResult.text =
                        buildString {
                            append("${response.city}\n")
                            append("${response.temperature}°\n")
                            append(response.condition)
                        }
                }

                is FastApiWeatherViewModel.State.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvResult.text = state.message
                }
            }
        }
    }
}