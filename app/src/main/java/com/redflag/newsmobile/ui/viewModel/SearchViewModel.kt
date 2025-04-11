package com.redflag.newsmobile.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redflag.newsmobile.BuildConfig
import com.redflag.newsmobile.data.remote.api.NewsAppApi
import com.redflag.newsmobile.data.remote.model.Article
import com.redflag.newsmobile.data.remote.service.RetrofitHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val _data = MutableStateFlow<List<Article>>(emptyList())
    val data: StateFlow<List<Article>?> = _data.asStateFlow()
    private val newsAppApi: NewsAppApi = RetrofitHelper().getInstance().create(NewsAppApi::class.java)

    fun searchWithFilters(searchQuery: String, country: String, category: String, language: String) {
        fetchDataWithFilters(searchQuery, country, category, language)
    }

    fun search(searchQuery: String) {
        Log.d("SearchViewModel:search", "Search query: $searchQuery")
        fetchData(searchQuery)
    }

    private fun fetchData(searchQuery: String) {
        Log.d("SearchViewModel:fetchData", "Search query: $searchQuery")
        viewModelScope.launch {
            _data.value = emptyList()

            try {
                val response = newsAppApi.searchNews(searchQuery, BuildConfig.NEWS_API_KEY)
                if (response.isSuccessful) {
                    Log.d("SearchViewModel", "Data fetched successfully")
                    _data.value = response.body()?.articles ?: emptyList()
                } else {
                    Log.d("SearchViewModel", "Error fetching data: ${response.code()} ${response.raw()}")
                    _data.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception fetching data: ${e.message}", e)
                _data.value = emptyList()
            }
        }
    }

    private fun fetchDataWithFilters(searchQuery: String, country: String, category: String, language: String) {
        viewModelScope.launch {
            _data.value = emptyList()
            try {
                val response = newsAppApi.searchNewsWithFilter(searchQuery, country, category, language)
                if (response.isSuccessful) {
                    Log.d("SearchViewModel", "Data fetched successfully")
                    _data.value = response.body()?.articles ?: emptyList()
                } else {
                    Log.d("SearchViewModel", "Error fetching data: ${response.code()} ${response.raw()}")
                    _data.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception fetching data: ${e.message}", e)
                _data.value = emptyList()
            }
        }
    }
}