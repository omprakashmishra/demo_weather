package com.omslab.weather.data.datasource.local

import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.mapper.LocationMapper
import com.omslab.weather.database.QueryDAO
import com.omslab.weather.domain.models.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

