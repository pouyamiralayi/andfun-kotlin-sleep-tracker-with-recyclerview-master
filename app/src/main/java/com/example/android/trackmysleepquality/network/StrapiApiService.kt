package com.example.android.trackmysleepquality.network

import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
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
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(API_URL)
        .build()

interface StrapiApiService {
    @GET("customers")
    fun getCustomers():
            Deferred<List<Customer>>

    @GET("sellers")
    fun getSellers():
            Deferred<List<Seller>>
}

object StrapiApi {
    val retrofitService: StrapiApiService by lazy {
        retrofit.create(StrapiApiService::class.java)
    }
}