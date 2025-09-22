package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.data.database.dao.WeatherDao
import com.assesment.gweather.data.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao
) {

    suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

    fun getWeatherHistory(uid: String): Flow<List<WeatherEntity>> {
        return weatherDao.getWeatherHistory(uid)
    }
}