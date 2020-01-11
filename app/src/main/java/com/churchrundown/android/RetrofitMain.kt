package com.churchrundown.android

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitMain {
    companion object Builder {
        var retrofit: Retrofit? = null

        fun build() {
            var BASE_URL = "http://192.168.43.47:3000/public/"

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}