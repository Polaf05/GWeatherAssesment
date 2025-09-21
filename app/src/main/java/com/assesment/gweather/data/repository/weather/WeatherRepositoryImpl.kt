package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.repository.auth.AuthRemoteDataSource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double) = remoteDataSource.getCurrentWeather(lat,lon)

}