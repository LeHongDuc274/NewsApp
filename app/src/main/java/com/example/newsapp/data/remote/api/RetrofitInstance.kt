package com.example.newsapp.data.remote.api

import com.example.newsapp.Contains.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val newsApi = retrofit.create(NewsApi::class.java)
    }
}