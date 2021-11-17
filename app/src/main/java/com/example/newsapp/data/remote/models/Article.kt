package com.example.newsapp.data.remote.models

import java.io.Serializable

data class Article(
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Serializable