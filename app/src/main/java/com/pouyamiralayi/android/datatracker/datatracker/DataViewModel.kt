package com.pouyamiralayi.android.datatracker.datatracker

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.pouyamiralayi.android.datatracker.database.Customer
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.network.ApiState
import com.pouyamiralayi.android.datatracker.network.CustomerDataSource
import com.pouyamiralayi.android.datatracker.network.CustomerDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DataViewModel(val customer_name: String, val customer_no: String, val _jwt: String) : ViewModel() {



    val customerName = MutableLiveData<String>()
    val customerNo = MutableLiveData<String>()
    val jwt = MutableLiveData<String>()


    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val customers : LiveData<PagedList<Customer>>
    val customersDataSource: LiveData<CustomerDataSource>
    val sellers = MutableLiveData<PagedList<Seller>>()
    val state : LiveData<ApiState>

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
//        coroutineScope.launch {
//            val temp = mutableListOf<Seller>()
//            sellers.value?.forEach {
//                if (it.product.toLowerCase().contains(query) ||
//                        it.productNo.toLowerCase().contains(query) ||
//                        it.description.toLowerCase().contains(query) ||
//                        it.rate.toLowerCase().contains(query) ||
//                        it.firstUnit.toLowerCase().contains(query) ||
//                        it.quantity.toLowerCase().contains(query)
//                ) {
//                    temp.add(it)
//                }
//            }
//            if (temp.size > 0) {
//                sellers.postValue(temp)
//            } else {
//                queryNotFound.postValue(true)
//            }
//        }
    }

    private fun searchCustomers(query: String) {
//        coroutineScope.launch {
//            val temp = mutableListOf<Customer>()
//            customers.value?.forEach {
//                if (it.description.toLowerCase().contains(query)
//                ) {
//                    temp.add(it)
//                }
//            }
//            if (temp.size > 0) {
//                customers.postValue(temp)
//            } else {
//                queryNotFound.postValue(true)
//            }
//        }
    }

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }

    init {
//        state.value = ApiState.LOADING
        queryNotFound.value = false
        _customersScreen.value = true
        customerName.value = customer_name
        customerNo.value = customer_no
        jwt.value = _jwt

        val customerDataSourceFactory = CustomerDataSourceFactory(jwt.value ?: "", customerNo.value
                ?: "")
        //getting the live data source from data source factory
        customersDataSource = customerDataSourceFactory.getCustomerLiveDataSource()
        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                /*TODO*/
                .setPageSize(10)
                .build()

        customers = LivePagedListBuilder(customerDataSourceFactory, pagedListConfig).build()
        state = Transformations.switchMap(customerDataSourceFactory.getCustomerLiveDataSource(), CustomerDataSource::state)

        owed.addSource(customers) {
            coroutineScope.launch {
                customers.value?.let {
                    val res = it.map { it.owed?.toDouble() ?: 0.0 }.sum()
                    owed.postValue(String.format("%,.0f", res.toDouble()))
                }
            }
        }
        owned.addSource(customers) {
            coroutineScope.launch {
                customers.value?.let {
                    val res = it.map { it.owned?.toDouble() ?: 0.0 }.sum()
                    owned.postValue(String.format("%,.0f", res.toDouble()))
                }
            }
        }
//        fetchCustomers()
//        fetchSellers()
    }

    fun fetchCustomers() {
//        coroutineScope.launch {
//
//            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers("Bearer ${jwt.value}", customerNo.value
//                    ?: "")
//            Log.i("Login", jwt.value)
//            try {
//                state.value = ApiState.LOADING
//                val resultList = getCustomersDeferred.await()
//                state.value = ApiState.DONE
//                customers.value = resultList
//            } catch (t: Throwable) {
//                state.value = ApiState.DONE
//                customers.value = listOf()
////                fetchError.value = "خطا!"
//                fetchError.value = t.message
//            }
//        }
    }

    fun fetchSellers() {
//        coroutineScope.launch {
//
//            val getCustomersDeferred = StrapiApi.retrofitService.getSellers("Bearer ${jwt.value}", customerNo.value
//                    ?: "")
//            Log.i("Login", jwt.value)
//            try {
//                state.value = ApiState.LOADING
//                val resultList = getCustomersDeferred.await()
//                state.value = ApiState.DONE
//                sellers.value = resultList
//            } catch (t: Throwable) {
//                state.value = ApiState.DONE
////                fetchError.value = "خطا!"
//                fetchError.value = t.message
//                sellers.value = listOf()
//            }
//        }
    }


    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}