package com.example.newsapp.network

data class NewsApiResult(
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)
