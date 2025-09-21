package com.assesment.gweather.data.network.model

class ApiException(
    override val message: String,
    val code: String
) : Exception(message)