package com.assesment.gweather.ui.model

import com.assesment.gweather.data.model.WeatherResponse

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}