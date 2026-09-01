package com.omslab.weather.presentation.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.omslab.weather.databinding.ActivityDashboardBinding
import com.omslab.weather.presentation.dashboard.adapter.DashboardPagerAdapter
import com.omslab.weather.presentation.loginReg.LoginAc
import com.omslab.weather.common.util.Constants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.logOut.setOnClickListener {
            viewModel.deleteOldLocations()
            intent = Intent(this, LoginAc::class.java)
            finish()
            startActivity(intent)
        }

        val adapter = DashboardPagerAdapter(this, supportFragmentManager)
        binding.tabsMain.setupWithViewPager(binding.vpMain)
        binding.vpMain.adapter = adapter

        // Check and request location permission first
        checkLocationPermissionAndGetLocation()
    }

    private fun checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            // Permission already granted, get location
            getLocation()
        }
    }

    private fun getLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        locationListener?.let {
            locationManager?.removeUpdates(it)
        }

        locationListener = LocationListener { location ->
            location.let {
                viewModel.storeLatLong(
                    it.latitude.toString(),
                    it.longitude.toString()
                )
            }
        }

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener!!
                )
                // Also try GPS provider as fallback
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener!!
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty()) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> {
                        getLocation()
                    }
                    PackageManager.PERMISSION_DENIED -> {
                        Toast.makeText(
                            applicationContext,
                            "Please allow permission for current location weather access.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Don't call getLocation() here to avoid infinite loop
                        // You might want to show a dialog or use a default location
                        // For example, set a default location or use IP-based location
                        viewModel.storeLatLong("40.7128", "-74.0060") // Default to NYC
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up location listener to prevent memory leaks
        locationListener?.let {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            try {
                locationManager?.removeUpdates(it)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }
}