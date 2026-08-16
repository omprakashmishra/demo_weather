package com.collabera.weather.presentation.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.collabera.weather.databinding.FragmentListWeatherBinding
import com.collabera.weather.presentation.viewmodel.DashBoardViewModel
import com.collabera.weather.presentation.ui.dashboard.adapter.WeatherListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListWeatherFm : Fragment() {

    lateinit var listAdapter: WeatherListAdapter
    private var binding: FragmentListWeatherBinding?=null
    private val viewModel: DashBoardViewModel by activityViewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        binding= FragmentListWeatherBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val pos = arguments?.getInt(ARG_SECTION_NUMBER)

        intRecyclerView()
        bindObservers()
    }

    private fun intRecyclerView(){
        listAdapter= WeatherListAdapter()
        binding!!.rvItems.adapter=listAdapter
    }

    private fun bindObservers() {
        viewModel.weatherList.observe(viewLifecycleOwner){ result->
            if(result!=null){
                listAdapter.updateData(result)
                listAdapter.notifyDataSetChanged()
                binding!!.tvSize.text="Total : "+result.size.toString()
            }else{
                binding!!.tvSize.text="No data found!"
            }
        }

    }

    companion object {
        private var ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(pos: Int) =  ListWeatherFm().apply {arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, pos)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}