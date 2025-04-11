package com.redflag.newsmobile.data.remote.api

import com.redflag.newsmobile.data.remote.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsAppApi {

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
    @GET("/v2/top-headlines?country=us&apiKey=d476f1b66ec3447490f84f2a20abc0ee")
    suspend fun getHeadLines(): Response<NewsResponse>

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
    @GET("v2/everything")
    suspend fun searchNews(@Query("q") queryText: String, @Query("apiKey") apiKey: String = "d476f1b66ec3447490f84f2a20abc0ee"): Response<NewsResponse>

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
    @GET("v2/everything/sources")
    suspend fun searchNewsWithFilter(@Query("q") queryText: String, @Query("apiKey") apiKey: String = "d476f1b66ec3447490f84f2a20abc0ee", @Query("country") country: String? = null, @Query("category") category: String? = null, @Query("language") language: String? = null): Response<NewsResponse>
}