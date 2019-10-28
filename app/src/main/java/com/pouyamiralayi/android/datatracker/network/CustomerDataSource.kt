package com.pouyamiralayi.android.datatracker.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Customer
import kotlinx.coroutines.*
import java.net.SocketTimeoutException


@Suppress("unused")
class CustomerDataSource(val jwt: String, val customerNo: String, val query:String) : PageKeyedDataSource<Int, Customer>() {

    private val apiJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val state = MutableLiveData<ApiState>()
    val fetchError = MutableLiveData<String>()


    val _limit = 10
    private val _start = 0


    init {
        state.postValue(ApiState.LOADING)
        fetchError.postValue("")
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Customer>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers("Bearer $jwt", customerNo, _start, _limit, query)
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
                fetchError.postValue("خطا!")
//                fetchError.value = t.message
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Customer>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers("Bearer $jwt", customerNo, params.key, _limit, query)
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
                fetchError.postValue("خطا!")
//                fetchError.value = t.message
            }
        }
    }

    private suspend fun totalCount(): Int {
        var resultCount = -1
        try {
            val getCustomersCountDeffered = StrapiApi.retrofitService.getCustomersCount("Bearer $jwt", customerNo)
            resultCount = getCustomersCountDeffered.await()
        }
        catch (t:Throwable){
            state.postValue(ApiState.DONE)
        }
        return resultCount
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Customer>) {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers("Bearer $jwt", customerNo, params.key, _limit, query)
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
                fetchError.postValue("خطا!")
//                fetchError.value = t.message
            }
        }

    }
}