package com.example.hw_3.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class GitService {

    fun provideApi() = retrofit.create<GitAPI>()

    private val retrofit by lazy(LazyThreadSafetyMode.NONE) {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}