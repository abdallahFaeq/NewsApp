package com.example.newsappgsu.api

import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WebService{
    @GET("sources")
    fun getNewsSources(
        @Query("apiKey") apiKey:String ?= null,
        @Query("language")lang:String ?= null,
        @Query("country")country:String ?= null
    ) : Call<SourcesResponse>

    @GET("everything")
    fun getNews(
        @Query("apiKey") key:String?=null,
        @Query("language")language:String? = null,
        @Query("sources")sources:String? = null,
        @Query("q")searchKeyWord:String? = null
    ):Call<NewsResponse>
}