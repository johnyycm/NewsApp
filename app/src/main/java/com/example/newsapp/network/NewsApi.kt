package com.example.newsapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "183daca270264bad86fc5b72972fb82a"

interface NewsApi {
    @GET("everything")
    suspend fun getNews(
        @Query("q") searchQuery: String,
        @Query("from") date: String,
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apikey: String = API_KEY
    ): Response<NewsApiResult>
}