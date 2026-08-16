package com.omslab.weather.data.dbcall.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omslab.weather.data.models.TableModel
import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.common.util.Constants.DataBaseVersion

@Database(  entities = [TableModel::class, UserLocationTableModel::class],   version = DataBaseVersion )
abstract class InitDataBase : RoomDatabase() {
    abstract fun getQueryDao(): QueryDAO
}