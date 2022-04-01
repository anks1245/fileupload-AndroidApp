package com.projectar.fileupload

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

class RetrofitConfig {
    private val BASE_URL = "http://172.19.59.115/fileupload/"
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}