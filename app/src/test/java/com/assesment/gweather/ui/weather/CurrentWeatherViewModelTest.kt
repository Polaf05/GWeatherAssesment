package com.assesment.gweather.ui.weather

import com.assesment.gweather.data.model.Main
import com.assesment.gweather.data.model.Sys
import com.assesment.gweather.data.model.Weather
import com.assesment.gweather.data.model.WeatherResponse
import com.assesment.gweather.data.repository.weather.WeatherRepository
import com.assesment.gweather.rule.MainDispatcherRule
import com.assesment.gweather.ui.model.WeatherUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentWeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: CurrentWeatherViewModel

    private val fakeWeatherResponse = WeatherResponse(
        city = "Test City", sys = Sys(
            country = "TC", sunrise = 123456L, sunset = 654321L
        ), main = Main(temp = 25.0), weather = listOf(Weather("Clear", "clear sky", "01d"))
    )

    @Before
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `uiState is Success when repository returns data`() = runTest {
        coEvery { repository.getCurrentWeather() } returns fakeWeatherResponse

        viewModel = CurrentWeatherViewModel(repository)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is WeatherUiState.Success)
        val successState = viewModel.uiState.value as WeatherUiState.Success
        assertEquals("Test City", successState.weather.city)
    }

    @Test
    fun `uiState is Error when repository throws exception`() = runTest {
        coEvery { repository.getCurrentWeather() } throws RuntimeException("Network error")

        viewModel = CurrentWeatherViewModel(repository)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is WeatherUiState.Error)
        val errorState = viewModel.uiState.value as WeatherUiState.Error
        assertEquals("Network error", errorState.message)
    }
}