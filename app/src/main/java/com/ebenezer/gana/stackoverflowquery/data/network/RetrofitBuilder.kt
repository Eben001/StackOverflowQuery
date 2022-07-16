package com.ebenezer.gana.stackoverflowquery.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
Builds Retrofit
 */
private const val BASE_URL = "https://api.stackexchange.com/"

private fun buildClient():OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


private fun buildRetrofit():Retrofit{
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(buildClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun buildApiService():StackOverflowApi =
    buildRetrofit().create(StackOverflowApi::class.java)
