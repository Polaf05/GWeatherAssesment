package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.BuildConfig
import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.network.api.WeatherApi
import com.assesment.gweather.data.network.model.SafeApiCall
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val api: WeatherApi): SafeApiCall() {

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse {
        return apiRequest {
            api.getCurrentWeather(
                lat = lat,
                lon = lon,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )
        }
    }
}