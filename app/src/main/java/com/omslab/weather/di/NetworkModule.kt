package com.omslab.weather.di

import android.util.Log
import com.omslab.weather.common.util.Constants
import com.omslab.weather.data.dbcall.remoteQuery.GetWeatherGQuery
import com.omslab.weather.data.dbcall.remoteQuery.FastApiWeatherServiceQuery
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * OkHttp client with Retrofit request/response logging.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("RETROFIT", message)
            Log.d("RETROFIT", "==============================")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Weather API Retrofit
     */
    @Provides
    @Singleton
    @WeatherRetrofit
    fun provideWeatherRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * FastAPI Retrofit
     */
    @Provides
    @Singleton
    @FastApiRetrofit
    fun provideFastApiRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.FAST_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Weather API service
     */
    @Provides
    @Singleton
    fun provideWeatherApiService(
        @WeatherRetrofit retrofit: Retrofit
    ): GetWeatherGQuery =
        retrofit.create(GetWeatherGQuery::class.java)

    /**
     * FastAPI service
     */
    @Provides
    @Singleton
    fun provideFastApiService(
        @FastApiRetrofit retrofit: Retrofit
    ): FastApiWeatherServiceQuery =
        retrofit.create(FastApiWeatherServiceQuery::class.java)
}