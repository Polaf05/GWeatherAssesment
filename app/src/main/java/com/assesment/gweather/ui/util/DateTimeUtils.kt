package com.assesment.gweather.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toHourMinute(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(this * 1000))
}
