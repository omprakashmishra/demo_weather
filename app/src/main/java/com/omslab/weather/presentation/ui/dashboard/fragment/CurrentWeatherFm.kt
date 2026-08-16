package com.collabera.weather.presentation.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.collabera.weather.R
import com.collabera.weather.databinding.FragmentCurrentWeatherBinding
import com.collabera.weather.models.UserLocationTableModel
import com.collabera.weather.presentation.viewmodel.DashBoardViewModel
import com.collabera.weather.common.util.Constants.PrimaryEmail
import com.collabera.weather.common.util.Constants.dateTimeAm
import com.collabera.weather.common.util.Constants.timeAm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFm : Fragment() {

    private var binding: FragmentCurrentWeatherBinding?=null
    private val viewModel: DashBoardViewModel by activityViewModels()



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        binding=FragmentCurrentWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val pos = arguments?.getInt(ARG_SECTION_NUMBER)
        bindObservers()
    }



    @SuppressLint("SetTextI18n")
    private fun bindObservers() {
        val email=viewModel.sp.getString(PrimaryEmail)
        Toast.makeText(activity, "WELCOME -$email", Toast.LENGTH_LONG).show()

        viewModel.weatherResponse.observe(viewLifecycleOwner) { result ->
            binding?.container?.apply {
                if(result.weather[0].icon.contains("d")){
                    //day
                    ivWeatherSun.setImageResource(R.drawable.sunshine)
                }else{
                    //night
                    ivWeatherSun.setImageResource(R.drawable.night_ic)
                }
                tvTemperature.text="Temperature- "+result.main.temp.toString()+" ℃"
                tvDescription.text="Description - "+result.weather[0].description
                tvCountry.text="Country - "+result.sys.country
                tvCity.text="City - "+result.name
                tvSunset.text="Sunset - "+viewModel.utcFormatted(result.sys.sunset, timeAm)
                tvSunrise.text="Sunrise - "+viewModel.utcFormatted(result.sys.sunrise, timeAm)



                // store in local db
                val userLocationTableModel=UserLocationTableModel(
                    icon = result.weather[0].icon,
                    email = email!!,
                    temperature = result.main.temp.toString(),
                    description = result.weather[0].description,
                    country = result.sys.country,
                    city = result.name,
                    sunset = viewModel.utcFormatted(result.sys.sunset, timeAm)!!,
                    sunrise = viewModel.utcFormatted(result.sys.sunrise, timeAm)!!,
                    entryDateTime = viewModel.utcFormatted(result.dt, dateTimeAm)!!
                )
                viewModel.enterUserLocation(userLocationTableModel)

            }

        }

    }


    //--------------------------------
    companion object {
        var ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pos: Int) =
            CurrentWeatherFm().apply {
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