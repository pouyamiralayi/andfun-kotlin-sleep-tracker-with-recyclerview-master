package com.example.android.trackmysleepquality.network

import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val API_URL = "http://10.30.205.75:1339"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(API_URL)
        .build()

interface StrapiApiService {
    @GET("customers")
    fun getCustomers():
            Call<Customer>

    @GET("sellers")
    fun getSellers():
            Call<Seller>
}

object StrapiApi {
    val retrofitService: StrapiApiService by lazy {
        retrofit.create(StrapiApiService::class.java)
    }
}