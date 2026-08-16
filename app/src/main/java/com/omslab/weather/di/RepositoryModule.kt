package com.omslab.weather.di

import com.omslab.weather.data.repository.LocationRepositoryImpl
import com.omslab.weather.data.repository.UserRepositoryImpl
import com.omslab.weather.data.repository.WeatherRepositoryImpl
import com.omslab.weather.domain.repository.ILocationRepository
import com.omslab.weather.domain.repository.IUserRepository
import com.omslab.weather.domain.repository.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        implementation: WeatherRepositoryImpl
    ): IWeatherRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        implementation: LocationRepositoryImpl
    ): ILocationRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: UserRepositoryImpl
    ): IUserRepository
}
