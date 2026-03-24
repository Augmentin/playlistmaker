package com.example.playlistmaker.network

import com.example.playlistmaker.network.itunes.CustomDateTypeAdapter
import com.example.playlistmaker.network.itunes.ItunesApi
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

// тест ошибки
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .message("Mock error")
            .body(
                "Server error"
                    .toResponseBody("text/plain".toMediaType())
            )
            .build()
    }
}

object ItunesClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.ITUNES_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
                    .create()
            )
        )
        .build();

    val api: ItunesApi by lazy {
        retrofit.create(ItunesApi::class.java)
    }

}