package com.example.mypokemon.api

import android.content.Context
import com.example.mypokemon.interfaces.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WebClient(val context: Context) {
    //Base Url variable
    private val BASE_URL = "https://pokeapi.co/api/v2/"

    //Api instance
    private var myApi: API? = null

    init {
        //init Retrofit
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(API::class.java)
    }

    fun api(): API? {
        return myApi
    }

}