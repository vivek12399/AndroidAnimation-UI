package com.digitalsoch.network

import android.content.Context
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AdminApi {

    /*@FormUrlEncoded
    @POST("employee/login")
    suspend fun adminLogin(@FieldMap hashMap: HashMap<String, Any>): Response<LoginResponse>*/


    companion object {
        operator fun invoke(context: Context): AdminApi {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context))
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://ecom.echoexitportfolio.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AdminApi::class.java)
        }
    }
}