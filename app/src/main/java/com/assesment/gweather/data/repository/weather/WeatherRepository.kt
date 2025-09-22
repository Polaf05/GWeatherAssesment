package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.data.database.entity.WeatherEntity
import com.assesment.gweather.data.model.Coordinates
import com.assesment.gweather.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(): WeatherResponse
    fun getWeatherHistory(): Flow<List<WeatherEntity>>
    suspend fun getCurrentLocation(): Coordinates?
}