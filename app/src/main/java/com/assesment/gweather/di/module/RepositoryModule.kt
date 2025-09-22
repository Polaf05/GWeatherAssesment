package com.assesment.gweather.di.module

import com.assesment.gweather.data.repository.auth.AuthRepository
import com.assesment.gweather.data.repository.auth.AuthRepositoryImpl
import com.assesment.gweather.data.repository.weather.WeatherRepository
import com.assesment.gweather.data.repository.weather.WeatherRepositoryImpl
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
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository
}