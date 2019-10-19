package com.example.android.trackmysleepquality.datatracker

import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.example.android.trackmysleepquality.network.StrapiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiState { LOADING, ERROR, DONE }
class DataViewModel(val customer_name: String, val customer_no: String) : ViewModel() {

    val customerName = MutableLiveData<String>()
    val customerNo = MutableLiveData<String>()

    val state = MutableLiveData<ApiState>()

    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val customers = MutableLiveData<List<Customer>>()
    val sellers = MutableLiveData<List<Seller>>()

    val owed = MediatorLiveData<String>()
    val owned = MediatorLiveData<String>()


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
        customerName.value = customer_name
        customerNo.value = customer_no

        owed.addSource(customers) {
            coroutineScope.launch {
                customers.value?.let {
                    val res = it.map { it.owed.toFloat() }.sum()
                    owed.postValue(String.format("%,.0f", res.toFloat()))
                }
            }
        }
        owned.addSource(customers) {
            coroutineScope.launch {
                customers.value?.let {
                    val res = it.map { it.owned.toFloat() }.sum()
                    owned.postValue(String.format("%,.0f", res.toFloat()))
                }
            }
        }
        fetchCustomers()
        fetchSellers()
    }

    fun fetchCustomers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers(customerNo.value
                    ?: "")
            try {
                state.value = ApiState.LOADING
                val resultList = getCustomersDeferred.await()
                state.value = ApiState.DONE
                customers.value = resultList
            } catch (t: Throwable) {
                state.value = ApiState.ERROR
                customers.value = listOf()
                fetchError.value = "خطا!"
//                fetchError.value = t.message
            }
        }
    }

    fun fetchSellers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers(customerNo.value ?: "")
            try {
                state.value = ApiState.LOADING
                val resultList = getCustomersDeferred.await()
                state.value = ApiState.DONE
                sellers.value = resultList
            } catch (t: Throwable) {
                state.value = ApiState.ERROR
                fetchError.value = "خطا!"
//                fetchError.value = t.message
                sellers.value = listOf()
            }
        }
    }


    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}