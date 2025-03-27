package com.redflag.newsmobile.data.remote.api

import com.redflag.newsmobile.data.remote.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsAppApi {
    //f887caf7b156483d97fabdd491b92fa5

//    @Headers({
//        "Accept: application/json",
//        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36"
//    })
    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
    @GET("/v2/top-headlines?country=us&apiKey=f887caf7b156483d97fabdd491b92fa5")
    suspend fun getHeadLines(): Response<NewsResponse>

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
    @GET("v2/everything")
    suspend fun searchNews(@Query("q") queryText: String, @Query("apiKey") apiKey: String = "f887caf7b156483d97fabdd491b92fa5"): Response<NewsResponse>
}