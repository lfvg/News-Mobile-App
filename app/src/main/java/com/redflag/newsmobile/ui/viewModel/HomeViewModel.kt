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

class HomeViewModel: ViewModel() {

    private val _data = MutableStateFlow<List<Article>>(emptyList())
    val data: StateFlow<List<Article>?> = _data.asStateFlow()
    private val newsAppApi: NewsAppApi = RetrofitHelper().getInstance().create(NewsAppApi::class.java)

    init {
        Log.d("HomeViewModel", "ViewModel initialized")
        fetchData()

    }

    private fun fetchData() {
        viewModelScope.launch {
            if (_data.value.isEmpty()) {
                _data.value = emptyList(); //reset data

                val response = newsAppApi.getHeadLines()
                if (response.isSuccessful) {
                    _data.value = response.body()?.articles ?: emptyList()
                } else {
                    Log.d(
                        "HomeViewModel",
                        "Error fetching data: " + response.code() + response.raw()
                    )
                }
            }
        }
    }
    fun refreshData() {
        viewModelScope.launch {
            _data.value = emptyList();
            try {
                val response = newsAppApi.getHeadLines()
                if (response.isSuccessful) {
                    Log.d("HomeViewModel", "Data refreshed successfully")
                    _data.value = response.body()?.articles ?: emptyList()
                } else {
                    Log.d("HomeViewModel", "Error refreshing data: " + response.code() + response.raw())
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error refreshing data: ${e.message}")
            }
        }
    }
}