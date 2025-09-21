package com.assesment.gweather.data.model

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
    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)
