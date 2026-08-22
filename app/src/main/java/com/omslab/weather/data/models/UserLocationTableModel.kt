package com.omslab.weather.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omslab.weather.common.util.Constants.LocationTable

@Entity(tableName = LocationTable)
data class UserLocationTableModel(
    var lat: String = "",
    var lon: String = "",
    var cityName: String = "",
    var country: String = "",
    var temperature: String = "",
    var description: String = "",
    var icon: String = "",
    var sunrise: String = "",
    var sunset: String = "",
    var entryDateTime: String = "",
    var email: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}