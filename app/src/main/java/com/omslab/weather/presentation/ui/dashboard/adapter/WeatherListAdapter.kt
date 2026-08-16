package com.omslab.weather.presentation.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omslab.weather.R
import com.omslab.weather.databinding.WeatherItemBinding
import com.omslab.weather.data.models.UserLocationTableModel

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.MyViewHolder>() {

    var list: List<UserLocationTableModel>? = null

    fun updateData(items: List<UserLocationTableModel>) {
        this.list = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WeatherItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.binding.apply {
            if (list!![pos].icon.contains("d")) {
                ivWeatherSun.setImageResource(R.drawable.sunshine)
            } else {
                ivWeatherSun.setImageResource(R.drawable.night_ic)
            }
            tvTemperature.text = "Temperature: ${list!![pos].temperature}°C"
            tvDescription.text = "Description: ${list!![pos].description}"
            tvCountry.text = "Country: ${list!![pos].country}"
            tvCity.text = "City: ${list!![pos].cityName}"
            tvSunset.text = "Sunset: ${list!![pos].sunset}"
            tvSunrise.text = "Sunrise: ${list!![pos].sunrise}"
            tvUpdateDateTime.text = list!![pos].entryDateTime
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    class MyViewHolder(itemView: WeatherItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: WeatherItemBinding

        init {
            binding = itemView
        }
    }
}