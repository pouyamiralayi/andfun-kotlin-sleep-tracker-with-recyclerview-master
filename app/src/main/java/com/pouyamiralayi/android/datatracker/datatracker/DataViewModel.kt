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
import androidx.lifecycle.Transformations
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class DataViewModel(private val customerName: String, private val customerNo: String, private val jwt: String) : ViewModel() {

    /*Data Source*/
    private val customerDataSourceFactory: CustomerDataSourceFactory
    private val customersDataSource: LiveData<CustomerDataSource>

    /*Api*/
    val customers: LiveData<PagedList<Customer>>

    @Suppress("MemberVisibilityCanBePrivate")
    fun searchCustomers(query: String) {
        customerDataSourceFactory.search(query)
        customers.value?.dataSource?.invalidate()
    }

    fun reload() {
        when (_customersScreen.value) {
            true -> searchCustomers("")
            else -> fetchSellers()
        }
    }

    val sellers = MutableLiveData<PagedList<Seller>>()
    val state: LiveData<ApiState>
    val owed = MediatorLiveData<String>()
    val owned = MediatorLiveData<String>()
    val fetchError = MutableLiveData<String>()


    /*Co-Routine*/
    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val queryNotFound = MutableLiveData<Boolean>()
    fun onQueryNotFoundCompleted() {
        queryNotFound.value = false
    }


    private val _customersScreen = MutableLiveData<Boolean>()
    val customersScreen: LiveData<Boolean>
        get() = _customersScreen

    init {
        queryNotFound.value = false
        _customersScreen.value = true

        //creating data source factory
        customerDataSourceFactory = CustomerDataSourceFactory(jwt, customerNo)
        //getting the live data source from data source factory
        customersDataSource = customerDataSourceFactory.getCustomerLiveDataSource()
        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                /*FIXME is 10 sufficient?*/
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
    }


    fun navigateToCustomers() {
        _customersScreen.value = true
    }

    fun navigateToSellers() {
        _customersScreen.value = false
    }

    @Deprecated("use search** instead")
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


    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
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