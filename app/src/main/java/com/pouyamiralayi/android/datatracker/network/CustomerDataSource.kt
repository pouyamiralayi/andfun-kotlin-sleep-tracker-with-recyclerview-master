package com.pouyamiralayi.android.datatracker.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Suppress("unused")
class CustomerDataSource(_jwt: String, _customerNo: String, _query:String) : PageKeyedDataSource<Int, Customer>() {

    private val query = _query
    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val state = MutableLiveData<ApiState>()


    val _limit = 10
    private val _start = 0

    val jwt = _jwt
    val customerNo = _customerNo

    init {
        state.postValue(ApiState.LOADING)
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
            } catch (t: Throwable) {
                state.postValue(ApiState.DONE)
//                fetchError.value = "خطا!"
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
//                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }
    }

    private suspend fun totalCount(): Int {
        val getCustomersCountDeffered = StrapiApi.retrofitService.getCustomersCount("Bearer $jwt", customerNo)
        return getCustomersCountDeffered.await()
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
//                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }

    }
}