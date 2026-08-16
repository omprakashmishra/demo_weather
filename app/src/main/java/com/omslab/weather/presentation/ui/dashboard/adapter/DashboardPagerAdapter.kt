package com.collabera.weather.presentation.ui.dashboard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.collabera.weather.R
import com.collabera.weather.presentation.ui.dashboard.fragment.CurrentWeatherFm
import com.collabera.weather.presentation.ui.dashboard.fragment.ListWeatherFm

class DashboardPagerAdapter (context: FragmentActivity,fm: FragmentManager):
    FragmentStatePagerAdapter(fm) {

    private var tabTitles: Array<String>

    init {
        tabTitles = context.resources.getStringArray(R.array.home_tab_array)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }

    override fun getCount(): Int {
       return tabTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            CurrentWeatherFm.newInstance(position)
        else
            ListWeatherFm.newInstance(position)
    }
}