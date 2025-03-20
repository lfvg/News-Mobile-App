package com.redflag.newsmobile.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.redflag.newsmobile.data.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {

    private val _data = MutableStateFlow<List<Article>>(emptyList())
    val data: StateFlow<List<Article>?> = _data.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        _data.value = emptyList(); //reset data
        Log.d("HomeViewModel", "Fetching data...")
    }
}