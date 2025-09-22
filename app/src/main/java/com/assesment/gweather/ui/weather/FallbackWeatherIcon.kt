package com.assesment.gweather.ui.weather

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun FallbackWeatherIcon(main: String?) {
    val isNight = isAfter6PM()
    val icon = when {
        isNight -> Icons.Default.NightlightRound
        main?.contains("rain", true) == true -> Icons.Default.Umbrella
        else -> Icons.Default.WbSunny
    }

    Icon(
        icon,
        contentDescription = "Fallback Weather Icon",
        modifier = Modifier.size(48.dp)
    )
}

fun isAfter6PM(): Boolean {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return hour >= 18
}