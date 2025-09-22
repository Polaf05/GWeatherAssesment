package com.assesment.gweather.data.model

import com.assesment.gweather.data.database.entity.WeatherEntity
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name")
    val city: String,

    @SerializedName("sys")
    val sys: Sys,

    @SerializedName("main")
    val main: Main,

    @SerializedName("weather")
    val weather: List<Weather>
)

data class Sys(
    @SerializedName("country")
    val country: String,

    @SerializedName("sunrise")
    val sunrise: Long,

    @SerializedName("sunset")
    val sunset: Long
)

data class Main(
    @SerializedName("temp")
    val temp: Double
)

data class Weather(
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)

fun Weather.getIconUrl(): String =
    "https://openweathermap.org/img/wn/${this.icon}@2x.png"

fun WeatherResponse.toEntity(uid: String): WeatherEntity {
    return WeatherEntity(
        uid = uid,
        city = city,
        country = sys.country,
        temperature = main.temp,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        main = weather.firstOrNull()?.main.toString(),
        description = weather.firstOrNull()?.description.toString(),
        icon = weather.firstOrNull()?.icon ?: "unknown",
        iconUrl = "https://openweathermap.org/img/wn/${weather.firstOrNull()?.icon}@2x.png"
    )
}
