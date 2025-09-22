package com.assesment.gweather.data.repository

import com.assesment.gweather.data.database.entity.WeatherEntity
import com.assesment.gweather.data.model.Coordinates
import com.assesment.gweather.data.model.Main
import com.assesment.gweather.data.model.Sys
import com.assesment.gweather.data.model.Weather
import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.repository.auth.AuthRepository
import com.assesment.gweather.data.repository.weather.WeatherLocalDataSource
import com.assesment.gweather.data.repository.weather.WeatherRemoteDataSource
import com.assesment.gweather.data.repository.weather.WeatherRepositoryImpl
import com.assesment.gweather.rule.MainDispatcherRule
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: WeatherRepositoryImpl
    private val remoteDataSource: WeatherRemoteDataSource = mockk()
    private val localDataSource: WeatherLocalDataSource = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk()

    private val fakeUser = mockk<FirebaseUser>().apply { every { uid } returns "test_uid" }
    private val fakeCoordinates = Coordinates(10.0, 20.0)
    private val fakeResponse = WeatherResponse(
        city = "Manila",
        sys = Sys(country = "PH", sunrise = 1000L, sunset = 2000L),
        main = Main(temp = 30.0),
        weather = listOf(Weather("Clear", "Sunny", "01d"))
    )

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource, authRepository)
    }

    @Test
    fun `getCurrentWeather returns and saves weather successfully`() = runTest {
        coEvery { remoteDataSource.getCurrentLocation() } returns fakeCoordinates
        coEvery { authRepository.currentUser() } returns fakeUser
        coEvery { remoteDataSource.getCurrentWeather(any(), any()) } returns fakeResponse

        val result = repository.getCurrentWeather()

        assertEquals("Manila", result.city)
        coVerify { localDataSource.insertWeather(any()) }
    }

    @Test(expected = IllegalStateException::class)
    fun `getCurrentWeather throws when location unavailable`() = runTest {
        coEvery { remoteDataSource.getCurrentLocation() } returns null
        coEvery { authRepository.currentUser() } returns fakeUser

        repository.getCurrentWeather()
    }

    @Test
    fun `getCurrentWeather propagates exception from remoteDataSource`() = runTest {
        coEvery { remoteDataSource.getCurrentLocation() } returns fakeCoordinates
        coEvery { authRepository.currentUser() } returns fakeUser
        coEvery {
            remoteDataSource.getCurrentWeather(
                any(),
                any()
            )
        } throws RuntimeException("Network error")

        assertThrows(IllegalStateException::class.java) {
            runBlocking { repository.getCurrentWeather() }
        }
    }

    @Test
    fun `getCurrentWeather handles null currentUser gracefully`() = runTest {
        coEvery { remoteDataSource.getCurrentLocation() } returns fakeCoordinates
        coEvery { authRepository.currentUser() } returns null
        coEvery { remoteDataSource.getCurrentWeather(any(), any()) } returns fakeResponse

        val result = repository.getCurrentWeather()

        assertEquals("Manila", result.city)
        coVerify { localDataSource.insertWeather(match { it.uid == "null" }) }
    }

    @Test
    fun `getWeatherHistory returns flow of weather entities`() = runTest {
        coEvery { authRepository.currentUser() } returns fakeUser
        val fakeEntities = listOf(
            WeatherEntity(
                uid = "test_uid",
                city = "Manila",
                country = "PH",
                temperature = 30.0,
                sunrise = 1000L,
                sunset = 2000L,
                main = "Clear",
                description = "Sunny",
                icon = "01d",
                iconUrl = "https://openweathermap.org/img/wn/01d.png"
            )
        )
        every { localDataSource.getWeatherHistory("test_uid") } returns flowOf(fakeEntities)

        val result = repository.getWeatherHistory().first()

        assertEquals(1, result.size)
        assertEquals("Manila", result[0].city)
    }

    @Test
    fun `getCurrentLocation delegates to remoteDataSource`() = runTest {
        coEvery { remoteDataSource.getCurrentLocation() } returns fakeCoordinates

        val result = repository.getCurrentLocation()

        assertEquals(fakeCoordinates, result)
    }
}