package com.assesment.gweather.data.database.entity

import android.accessibilityservice.GestureDescription
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uid: String,
    val city: String,
    val country: String,
    val temperature: Double,
    val sunrise: Long,
    val sunset: Long,
    val main: String,
    val description: String,
    val icon: String,
    val iconUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)