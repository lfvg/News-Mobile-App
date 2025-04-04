package com.redflag.newsmobile.data.remote.model

import java.net.URL


data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: URL,
    val urlToImage: URL?,
    val publishedAt: String?,
    val content: String
    ){

}