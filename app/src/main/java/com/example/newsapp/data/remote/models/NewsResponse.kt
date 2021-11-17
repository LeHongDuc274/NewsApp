package com.example.newsapp.data.remote.models

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)