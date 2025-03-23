package com.redflag.newsmobile.data.remote.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
