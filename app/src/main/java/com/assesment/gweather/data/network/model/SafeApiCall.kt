package com.assesment.gweather.data.network.model

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

abstract class SafeApiCall {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()

            error?.let {
                try {
                    val jsonObj = JSONObject(it)
                    message.append(jsonObj.optString("message"))
                } catch (e: JSONException) {
                    message.append("Unknown error format")
                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")

            throw ApiException(message.toString(), "Error Code: ${response.code()}")
        }
    }

    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): String {
        val response = call.invoke()
        if (response.isSuccessful) {
            return (response.body() as? ResponseBody)?.string().orEmpty()
        } else {
            throw HttpException(response)
        }
    }
}