package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.data.database.entity.WeatherEntity
import com.assesment.gweather.data.model.Coordinates
import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.model.toEntity
import com.assesment.gweather.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val authRepository: AuthRepository
) : WeatherRepository {

    override suspend fun getCurrentWeather(): WeatherResponse {
        val coordinates = getCurrentLocation()
            ?: throw IllegalStateException("Location unavailable")

        val uid = authRepository.currentUser()?.uid.toString()

        return try {
            val response = remoteDataSource.getCurrentWeather(
                coordinates.lat,
                coordinates.lon
            )
            localDataSource.insertWeather(response.toEntity(uid))
            response
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch weather", e)
        }
    }

    override fun getWeatherHistory(): Flow<List<WeatherEntity>> {
        val uid = authRepository.currentUser()?.uid.toString()
        return localDataSource.getWeatherHistory(uid)
    }

    override suspend fun getCurrentLocation(): Coordinates? =
        remoteDataSource.getCurrentLocation()
}
