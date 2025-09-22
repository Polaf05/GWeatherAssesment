package com.assesment.gweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assesment.gweather.data.repository.weather.WeatherRepository
import com.assesment.gweather.ui.model.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        fetchCurrentWeather()
    }

    fun fetchCurrentWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _uiState.value = try {
                WeatherUiState.Success(repository.getCurrentWeather())
            } catch (e: Exception) {
                WeatherUiState.Error(
                    e.message ?: "Failed to load weather. Please check permissions or network."
                )
            }
        }
    }
}