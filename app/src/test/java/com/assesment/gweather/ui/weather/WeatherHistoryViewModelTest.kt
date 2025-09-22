package com.assesment.gweather.ui.weather

import app.cash.turbine.test
import com.assesment.gweather.data.database.entity.WeatherEntity
import com.assesment.gweather.data.repository.weather.WeatherRepository
import com.assesment.gweather.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherHistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: WeatherRepository = mockk()
    private lateinit var viewModel: WeatherHistoryViewModel

    private val fakeWeatherHistory = listOf(
        WeatherEntity(
            id = 1,
            uid = "user1",
            city = "Manila",
            country = "PH",
            temperature = 30.5,
            sunrise = 123456789,
            sunset = 987654321,
            main = "Clear",
            description = "Sunny",
            icon = "01d",
            iconUrl = "http://example.com/icon.png"
        )
    )

    @Before
    fun setup() {
        coEvery { repository.getWeatherHistory() } returns flowOf(emptyList())
    }

    @Test
    fun `weatherHistory starts with empty list`() = runTest {
        viewModel = WeatherHistoryViewModel(repository)

        viewModel.weatherHistory.test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `weatherHistory emits history from repository`() = runTest {
        coEvery { repository.getWeatherHistory() } returns flowOf(fakeWeatherHistory)

        viewModel = WeatherHistoryViewModel(repository)

        viewModel.weatherHistory.test {
            val item = awaitItem()
            assertEquals(1, item.size)
            assertEquals("Manila", item[0].city)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `weatherHistory updates when repository emits new data`() = runTest {
        val historyFlow = MutableSharedFlow<List<WeatherEntity>>(replay = 1)
        coEvery { repository.getWeatherHistory() } returns historyFlow

        viewModel = WeatherHistoryViewModel(repository)

        viewModel.weatherHistory.test {
            historyFlow.emit(emptyList())
            assertTrue(awaitItem().isEmpty())

            historyFlow.emit(fakeWeatherHistory)
            val item = awaitItem()
            assertEquals("Manila", item[0].city)

            cancelAndIgnoreRemainingEvents()
        }
    }
}