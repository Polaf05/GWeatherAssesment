package com.assesment.gweather.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.assesment.gweather.data.model.getIconUrl
import com.assesment.gweather.ui.model.WeatherUiState
import com.assesment.gweather.ui.theme.ClearDay
import com.assesment.gweather.ui.theme.ClearNight
import com.assesment.gweather.ui.theme.RainyDay
import com.assesment.gweather.ui.theme.RainyNight
import com.assesment.gweather.ui.theme.getWeatherBackground
import com.assesment.gweather.ui.util.toHourMinute
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentWeatherScreen(viewModel: CurrentWeatherViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var iconState by remember { mutableStateOf(true) }

    when (state) {
        is WeatherUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is WeatherUiState.Success -> {
            val weather = (state as WeatherUiState.Success).weather
            val weatherInfo = weather.weather.firstOrNull()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        getWeatherBackground(weatherInfo?.main.toString())
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // City Name
                Text(
                    text = "${weather.city}, ${weather.sys.country}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Current Temperature
                Text(
                    text = "${weather.main.temp}°C",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Weather Description
                Text(text = weatherInfo?.description?.replaceFirstChar { it.uppercase() } ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f))

                Spacer(modifier = Modifier.height(24.dp))

                // Weather Icon
                if (iconState && weatherInfo != null && weatherInfo.icon.isNotBlank()) {
                    AsyncImage(
                        model = weatherInfo.getIconUrl(),
                        contentDescription = weatherInfo.main,
                        modifier = Modifier.size(80.dp),
                        onError = { iconState = false })
                } else {
                    FallbackWeatherIcon(weatherInfo?.main)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Sunrise / Sunset Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.WbSunny,
                                contentDescription = "Sunrise",
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Sunrise", color = Color.White)
                            Text(weather.sys.sunrise.toHourMinute(), color = Color.White)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.NightlightRound,
                                contentDescription = "Sunset",
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Sunset", color = Color.White)
                            Text(weather.sys.sunset.toHourMinute(), color = Color.White)
                        }
                    }
                }
            }
        }

        is WeatherUiState.Error -> {
            val message = (state as WeatherUiState.Error).message
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error: $message")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { viewModel.fetchCurrentWeather() }) {
                    Text("Retry")
                }
            }
        }
    }
}

