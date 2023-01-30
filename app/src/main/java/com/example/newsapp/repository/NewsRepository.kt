package com.example.newsapp.repository

import com.example.newsapp.network.News
import com.example.newsapp.network.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi
) {

    suspend fun getNews(searchQuery: String): Result<List<News>>{
        return withContext(Dispatchers.IO){
            val sdf = SimpleDateFormat("yyyy-mm-dd")
            val currentDate = sdf.format(Date())
            val response = newsApi.getNews(searchQuery, currentDate)
            if(response.body()!!.articles.isEmpty()){
                Result.Error("No News found")
            }else{
                Result.Success(response.body()!!.articles)
            }
        }
    }


}

/**
 * Result class to define result for either Success or Error
 */
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        const val GENERIC_ERROR_MSG = "Something went wrong"
        const val NETWORK_ERROR_MSG = "No internet. Please check your network!"
    }

    class Success<T>(data: T) : Result<T>(data = data)
    class Error<T>(errorMsg: String) : Result<T>(message = errorMsg)
}