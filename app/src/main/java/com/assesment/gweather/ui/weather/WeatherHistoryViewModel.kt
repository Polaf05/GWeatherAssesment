package com.assesment.gweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assesment.gweather.data.database.entity.WeatherEntity
import com.assesment.gweather.data.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class WeatherHistoryViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val weatherHistory: StateFlow<List<WeatherEntity>> = repository.getWeatherHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}