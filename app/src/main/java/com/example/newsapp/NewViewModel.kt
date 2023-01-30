package com.example.newsapp

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.network.News
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel(){

    private val _newStateFlow = MutableStateFlow<ViewState<List<News>>>(ViewState.None)
    val newsStateFlow = _newStateFlow.asStateFlow()

    fun loadSearchResult(searchQuery: String){
        _newStateFlow.value = ViewState.Loading
        viewModelScope.launch {
            val result = newsRepository.getNews(searchQuery)
            when(result){
                is Result.Success -> _newStateFlow.value = ViewState.Loaded(result.data!!)
                else -> _newStateFlow.value = ViewState.Error(result.message!!)
            }
        }
    }

}

sealed class ViewState<out T> {
    object None : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    class Loaded<T>(val data: T) : ViewState<T>()
    class Error(val errorMsg: String) : ViewState<Nothing>()
}