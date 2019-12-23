package com.churchrundown.android

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitMain {
    companion object Builder {
        var retrofit: Retrofit? = null

        fun build() {
            var BASE_URL = "http://10.236.218.155:3000/public/"

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}