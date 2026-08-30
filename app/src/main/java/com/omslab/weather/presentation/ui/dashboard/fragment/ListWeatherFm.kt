package com.omslab.weather.presentation.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.omslab.weather.databinding.FragmentListWeatherBinding
import com.omslab.weather.presentation.ui.dashboard.DashboardViewModel
import com.omslab.weather.presentation.ui.dashboard.adapter.WeatherListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListWeatherFm : Fragment() {

    lateinit var listAdapter: WeatherListAdapter
    private var binding: FragmentListWeatherBinding? = null
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bindObservers()
    }

    private fun initRecyclerView() {
        listAdapter = WeatherListAdapter { id ->
            viewModel.deleteListLocation(id)
            Toast.makeText(
                requireContext(),
                "Location deleted successfully",
                Toast.LENGTH_LONG
            ).show()
        }
        binding?.rvItems?.adapter = listAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindObservers() {
        viewModel.weatherList.observe(viewLifecycleOwner) { result ->
            if (result != null && result.isNotEmpty()) {
                listAdapter.updateData(result)
                listAdapter.notifyDataSetChanged()
                binding?.tvSize?.text = "Total : ${result.size}"
            } else {
                binding?.tvSize?.text = "No data found!"
            }
        }
    }

    companion object {
        private var ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pos: Int) = ListWeatherFm().apply {
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