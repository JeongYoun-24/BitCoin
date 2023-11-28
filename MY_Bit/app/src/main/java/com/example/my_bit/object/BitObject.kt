package com.example.my_bit.`object`

import com.example.my_bit.interfaces.BitAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BitObject {
    private const val BASE_URL = "https://api.upbit.com/"

    private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService : BitAPI by lazy { getRetrofit.create(BitAPI::class.java) }
}


