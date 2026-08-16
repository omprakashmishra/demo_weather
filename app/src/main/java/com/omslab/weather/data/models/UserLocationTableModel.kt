package com.omslab.weather.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omslab.weather.common.util.Constants.LocationTable


@Entity(  tableName = LocationTable)
data class UserLocationTableModel(
    var icon: String,
    var email: String,
    var temperature: String,
    var description: String,
    var country:String,
    var city:String,
    var sunrise:String,
    var sunset:String,
    var entryDateTime:String

){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}
