package com.digitalsoch.network

import com.digitalsoch.utils.Exceptions
import retrofit2.Response

abstract class ApiRequest {
    suspend fun <T : Any> apiRequest(apiCall: suspend () -> Response<T>): T {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return body
                } else {
                    throw Exceptions.ApiException("Response body is null")
                }
            } else {
                throw Exceptions.ApiException("Error: ${response.code()} ${response.message()}")
            }
        } catch (exception: Exception) {
            throw Exceptions.ApiException(exception.message ?: "Unknown error")
        }
    }
}
