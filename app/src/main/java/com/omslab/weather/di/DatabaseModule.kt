package com.omslab.weather.di

import android.content.Context
import androidx.room.Room
import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.dbcall.local.InitDataBase
import com.omslab.weather.data.dbcall.local.QueryDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): InitDataBase =
        Room.databaseBuilder(
            context,
            InitDataBase::class.java,
            Constants.DataBaseName
        ).build()

    @Provides
    fun provideQueryDao(database: InitDataBase): QueryDAO =
        database.getQueryDao()

    @Provides
    @Singleton
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): MySharedPreference =
        MySharedPreference(context)
}
