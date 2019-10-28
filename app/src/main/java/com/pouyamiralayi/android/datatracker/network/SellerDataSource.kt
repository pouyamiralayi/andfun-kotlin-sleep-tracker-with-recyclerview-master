package com.pouyamiralayi.android.datatracker.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Seller
import kotlinx.coroutines.*
import java.net.SocketTimeoutException


@Suppress("unused")
class SellerDataSource(val jwt: String, val sellerNon: String, private val query:String) : PageKeyedDataSource<Int, Seller>() {

    private val apiJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val state = MutableLiveData<ApiState>()


    val _limit = 10
    private val _start = 0


    init {
        state.postValue(ApiState.LOADING)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Seller>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers("Bearer $jwt", sellerNon, _start, _limit, query)
            Log.i("Login", jwt)
            try {
                state.postValue(ApiState.LOADING)
                val resultList = getCustomersDeferred.await()
                callback.onResult(resultList, null, _start + _limit)
                state.postValue(ApiState.DONE)
            }
            catch (e: SocketTimeoutException){
                state.postValue(ApiState.ERROR)
            }
            catch (t: Throwable) {
                state.postValue(ApiState.DONE)
//                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Seller>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers("Bearer $jwt", sellerNon, params.key, _limit, query)
            Log.i("Login", jwt)
            val adjacentKey = if (params.key < totalCount()) {
                params.key + _limit
            } else {
                null
            }
            try {
                state.postValue(ApiState.LOADING)
                val resultList = getCustomersDeferred.await()
                callback.onResult(resultList, adjacentKey)
                state.postValue(ApiState.DONE)
            } catch (t: Throwable) {
                state.postValue(ApiState.DONE)
//                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }
    }

    private suspend fun totalCount(): Int {
        var resultCount = -1
        try {
            val getCustomersCountDeffered = StrapiApi.retrofitService.getSellersCount("Bearer $jwt", sellerNon)
            resultCount = getCustomersCountDeffered.await()
        }
        catch (t:Throwable){
            state.postValue(ApiState.DONE)
        }
        return resultCount
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Seller>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers("Bearer $jwt", sellerNon, params.key, _limit, query)
            Log.i("Login", jwt)
            val adjacentKey = if (params.key >= _limit) {
                params.key - _limit
            } else {
                null
            }
            try {
                state.postValue(ApiState.LOADING)
                val resultList = getCustomersDeferred.await()
                callback.onResult(resultList, adjacentKey)

                state.postValue(ApiState.DONE)
            } catch (t: Throwable) {
                state.postValue(ApiState.DONE)
//                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }

    }
}