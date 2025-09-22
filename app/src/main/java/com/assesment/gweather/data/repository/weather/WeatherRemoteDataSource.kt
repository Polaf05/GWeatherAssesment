package com.assesment.gweather.data.repository.weather

import com.assesment.gweather.BuildConfig
import com.assesment.gweather.data.model.Coordinates
import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.network.api.WeatherApi
import com.assesment.gweather.data.network.model.SafeApiCall
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WeatherRemoteDataSource @Inject constructor(
    private val api: WeatherApi,
    private val fusedLocationClient: FusedLocationProviderClient
) : SafeApiCall() {

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse {
        return apiRequest {
            api.getCurrentWeather(
                lat = lat,
                lon = lon,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )
        }
    }

    suspend fun getCurrentLocation(): Coordinates? =
        suspendCoroutine { continuation ->
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                            CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    }
                ).addOnSuccessListener { location ->
                    continuation.resume(location?.let {
                        Coordinates(
                            it.latitude,
                            it.longitude
                        )
                    })
                }.addOnFailureListener {
                    continuation.resumeWithException(throw it)
                }
            } catch (e: SecurityException) {
                continuation.resumeWithException(throw e)
            }
        }

}