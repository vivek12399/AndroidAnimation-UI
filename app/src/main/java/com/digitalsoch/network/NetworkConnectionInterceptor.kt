package com.digitalsoch.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.digitalsoch.utils.Exceptions


import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw Exceptions.ApiException("Waiting for Internet Connection")
        }
        val requestBuilder =chain.request().newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        return chain.proceed(requestBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(): Boolean {
        var result: Boolean
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also { connectivityManager ->
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return result
        }
    }
}