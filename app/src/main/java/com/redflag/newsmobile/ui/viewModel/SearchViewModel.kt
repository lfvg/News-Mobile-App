package com.redflag.newsmobile.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun search(searchQuery: String) {
        Log.d("SearchViewModel:search", "Search query: $searchQuery")
        fetchData(searchQuery)
    }
    private fun fetchData(searchQuery: String) {
        Log.d("SearchViewModel:fetchData", "Search query: $searchQuery")

        viewModelScope.launch {
            _data.value = emptyList(); //reset data
            val response = newsAppApi.searchNews(searchQuery)
            if(response.isSuccessful) {
                Log.d("SearchViewModel", "Data fetched successfully")
                _data.value = response.body()?.articles ?: emptyList()
            }
            else {
                Log.d("SerachViewModel", "Error fetching data: " + response.code() + response.raw())
            }
        }
    }

}