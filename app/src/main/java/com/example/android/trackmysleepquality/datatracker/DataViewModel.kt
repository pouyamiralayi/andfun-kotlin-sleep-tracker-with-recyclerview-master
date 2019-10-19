package com.example.android.trackmysleepquality.datatracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.example.android.trackmysleepquality.network.StrapiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiState { LOADING, ERROR, DONE }
class DataViewModel : ViewModel() {


    val state = MutableLiveData<ApiState>()

    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val customers = MutableLiveData<List<Customer>>()
    val sellers = MutableLiveData<List<Seller>>()

    val fetchError = MutableLiveData<String>()

    val queryNotFound = MutableLiveData<Boolean>()

    fun onQueryNotFoundCompleted() {
        queryNotFound.value = false
    }

    private val _customersScreen = MutableLiveData<Boolean>()
    val customersScreen: LiveData<Boolean>
        get() = _customersScreen

    fun reload() {
        when (_customersScreen.value) {
            true -> fetchCustomers()
            else -> fetchSellers()
        }
    }


    fun navigateToCustomers() {
        _customersScreen.value = true
    }

    fun navigateToSellers() {
        _customersScreen.value = false
    }

    fun query(query: String) {
        when (_customersScreen.value) {
            true -> searchCustomers(query)
            else -> searchSellers(query)
        }
    }

    private fun searchSellers(query: String) {
        coroutineScope.launch {
            val temp = mutableListOf<Seller>()
            sellers.value?.forEach {
                if (it.product.toLowerCase().contains(query) ||
                        it.productNo.toLowerCase().contains(query) ||
                        it.description.toLowerCase().contains(query) ||
                        it.rate.toLowerCase().contains(query) ||
                        it.firstUnit.toLowerCase().contains(query) ||
                        it.quantity.toLowerCase().contains(query)
                ) {
                    temp.add(it)
                }
            }
            if (temp.size > 0) {
                sellers.postValue(temp)
            } else {
                queryNotFound.postValue(true)
            }
        }
    }

    private fun searchCustomers(query: String) {
        coroutineScope.launch {
            val temp = mutableListOf<Customer>()
            customers.value?.forEach {
                if (it.description.toLowerCase().contains(query)
                ) {
                    temp.add(it)
                }
            }
            if (temp.size > 0) {
                customers.postValue(temp)
            } else {
                queryNotFound.postValue(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }

    init {
        state.value = ApiState.LOADING
        queryNotFound.value = false
        _customersScreen.value = true
        fetchCustomers()
        fetchSellers()
    }

    fun fetchCustomers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers()
            try {
//                state.value = ApiState.LOADING
                val resultList = getCustomersDeferred.await()
                state.value = ApiState.DONE
                customers.value = resultList
            } catch (t: Throwable) {
                state.value = ApiState.ERROR
                customers.value = listOf()
                fetchError.value = t.message
            }
        }
    }

    fun fetchSellers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers()
            try {
//                state.value = ApiState.LOADING
                val resultList = getCustomersDeferred.await()
                state.value = ApiState.DONE
                sellers.value = resultList
            } catch (t: Throwable) {
                state.value = ApiState.ERROR
                fetchError.value = t.message
                sellers.value = listOf()
            }
        }
    }


    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}