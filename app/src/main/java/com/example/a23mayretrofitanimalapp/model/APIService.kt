package com.example.a23mayretrofitanimalapp.model

import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("animals/rand")
    fun getRandomAnimal(): Call<Animals>

}