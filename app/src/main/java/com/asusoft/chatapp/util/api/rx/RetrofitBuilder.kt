package com.asusoft.chatapp.util.api.rx

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(
    var url: String
) {
    var baseUrl :String = url

    private fun createClientAuth(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder

            // set timeout 10s
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)

            .addNetworkInterceptor(
                //로그 전부 찍음
                HttpLoggingInterceptor()
                    .setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
            )

        return okHttpClientBuilder.build()
    }

    fun build(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createClientAuth())
            .build()
    }

}