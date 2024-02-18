package com.example.newsappgsu.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager {
    companion object{
        private var retrofit: Retrofit ?= null

        var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(object :
            HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.e("api",message)
            }
        })
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = OkHttpClient.Builder().addInterceptor(interceptor = interceptor).build()


        private fun getRetrofitInstance():Retrofit{

            if (retrofit == null){
                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://newsapi.org/v2/")
                    .client(client)
                    .build()
            }
            return retrofit!!
        }
        fun getApis():WebService{
            return getRetrofitInstance()
                .create(WebService::class.java)
        }
    }


}