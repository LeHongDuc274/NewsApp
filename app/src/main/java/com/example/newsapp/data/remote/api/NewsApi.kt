package com.example.newsapp.data.remote.api

import com.example.newsapp.Contains.MY_API_KEY
import com.example.newsapp.data.remote.models.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") countryKey: String = "us",
        @Query("category") categoryKey: String = "business",
        @Query("apiKey") apiKey:String = MY_API_KEY
    ): Call<NewsResponse>

    @GET("v2/everything")
    fun getSearchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey:String = MY_API_KEY
    ): Call<NewsResponse>

}