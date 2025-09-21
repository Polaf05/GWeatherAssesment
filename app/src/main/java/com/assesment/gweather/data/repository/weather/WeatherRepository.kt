package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse
}