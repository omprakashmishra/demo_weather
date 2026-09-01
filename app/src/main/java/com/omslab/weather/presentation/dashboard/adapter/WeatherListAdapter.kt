package com.omslab.weather.presentation.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omslab.weather.R
import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.databinding.WeatherItemBinding

class WeatherListAdapter(
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<WeatherListAdapter.MyViewHolder>() {

    private var list: List<UserLocationTableModel> = emptyList()

    fun updateData(items: List<UserLocationTableModel>) {
        list = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val binding = WeatherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val location = list[position]

        holder.binding.apply {

            if (location.icon.contains("d")) {
                ivWeatherSun.setImageResource(R.drawable.sunshine)
            } else {
                ivWeatherSun.setImageResource(R.drawable.night_ic)
            }

            tvTemperature.text =
                "Temperature: ${location.temperature}°C"

            tvDescription.text =
                "Description: ${location.description}"

            tvCountry.text =
                "Country: ${location.country}"

            tvCity.text =
                "City: ${location.cityName}"

            tvSunset.text =
                "Sunset: ${location.sunset}"

            tvSunrise.text =
                "Sunrise: ${location.sunrise}"

            tvUpdateDateTime.text =
                location.entryDateTime

            // Delete icon
            ivAction.setImageResource(R.drawable.remove)
            //ivAction.visibility = View.VISIBLE

            ivAction.setOnClickListener {
                if (location.id == null)
                    return@setOnClickListener
                onDeleteClick(location.id!!)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(
        val binding: WeatherItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}