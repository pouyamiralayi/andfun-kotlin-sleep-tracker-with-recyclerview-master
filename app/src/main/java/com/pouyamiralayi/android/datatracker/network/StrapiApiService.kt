package com.pouyamiralayi.android.datatracker.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pouyamiralayi.android.datatracker.database.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

enum class ApiState { LOADING, ERROR, DONE, DISCONNECTED }


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

    @GET("customers/owed")
    fun getOwed(@Header("Authorization") token: String, @Query("customer_no") customer_no: String, @Query("description_contains") query: String?, @Query("date_gte") dateFrom: String?, @Query("date_lt") dateTo: String?):
            Deferred<OwedResponse>

    @GET("customers/count")
    fun getCustomersCount(@Header("Authorization") token: String, @Query("customer_no") customer_no: String):
            Deferred<Int>

    @GET("sellers/count")
    fun getSellersCount(@Header("Authorization") token: String, @Query("seller_no") seller_no: String):
            Deferred<Int>


    @GET("customers")
    fun getCustomers(@Header("Authorization") token: String, @Query("customer_no") customer_no: String, @Query("_start") start: Int, @Query("_limit") limit: Int, @Query("description_contains") query: String, @Query("date_gte") dateFrom: String?, @Query("date_lt") dateTo: String?):
            Deferred<List<Customer>>

    @GET("sellers")
    fun getSellers(@Header("Authorization") token: String, @Query("seller_no") seller_no: String, @Query("_start") start: Int, @Query("_limit") limit: Int, @Query("product_contains") query: String, @Query("date_gte") dateFrom: String?, @Query("date_lt") dateTo: String?):
            Deferred<List<Seller>>

    @POST("login")
    @FormUrlEncoded
    fun login(@Field("identifier") identifier: String, @Field("password") password: String):
            Deferred<LoginResp>

    @GET("users/me")
    fun auth(@Header("Authorization") token: String): Deferred<User>

}

object StrapiApi {
    val retrofitService: StrapiApiService by lazy {
        retrofit.create(StrapiApiService::class.java)
    }
}