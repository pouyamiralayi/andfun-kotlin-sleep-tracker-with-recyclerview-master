package com.pouyamiralayi.android.datatracker.datatracker

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pouyamiralayi.android.datatracker.database.Customer
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.network.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException


class DataViewModel(val customerName: String, val customerNo: String, private val jwt: String) : ViewModel() {

    /*Data Source*/
    private val customerDataSourceFactory: CustomerDataSourceFactory
    private val customersDataSource: LiveData<CustomerDataSource>
    private val sellerDataSourceFactory: SellerDataSourceFactory
    private val sellerDataSource: LiveData<SellerDataSource>

    /*Api*/
    val customers: LiveData<PagedList<Customer>>
    val fetchErrorCustomers: LiveData<String>
    private val stateCustomers: LiveData<ApiState>
    val owed = MutableLiveData<String>()
    val owned = MutableLiveData<String>()
    val rem = MutableLiveData<String>()
    val plus = MutableLiveData<Boolean>()

    /*Date Query*/
    val dateFrom = MutableLiveData<String?>()
    val dateFromPersian = MutableLiveData<String?>()
    val dateTo = MutableLiveData<String?>()
    val dateToPersian = MutableLiveData<String?>()

    @Suppress("MemberVisibilityCanBePrivate")
    fun searchCustomers(query: String, dateFrom: String?, dateTo: String?) {
        customerDataSourceFactory.search(query, dateFrom, dateTo)
        customers.value?.dataSource?.invalidate()
    }


    fun reload() {
        dateFrom.value = null
        dateFromPersian.value = null
        dateTo.value = null
        dateToPersian.value = null
        when (_customersScreen.value) {
            true -> {
                searchCustomers("", dateFrom.value, dateTo.value)
                fetchOwed()
            }
            else -> searchSellers("", dateFrom.value, dateTo.value)
        }
    }

    private fun fetchOwed() {
        coroutineScope.launch {
            val getCustomersOwed = StrapiApi.retrofitService.getOwed("Bearer $jwt", customerNo)
            try {
                val result = getCustomersOwed.await()
                owed.postValue("بدهکار: " + String.format("%,.0f", result.owed.toDouble()))
                owned.postValue("بستانکار: " + String.format("%,.0f", result.owned.toDouble()))
                rem.postValue("مانده: " + String.format("%,.0f", result.rem.toDouble()))
                plus.postValue(result.plus)
            } catch (e: SocketTimeoutException) {

            } catch (t: Throwable) {

            }
        }
    }

    fun listIsEmpty(): Boolean {
        return when (_customersScreen.value) {
            true -> (customers.value?.isEmpty() ?: true)
            else -> (sellers.value?.isEmpty() ?: true)
        }
    }

    val sellers: LiveData<PagedList<Seller>>
    val fetchErrorSellers: LiveData<String>
    private val stateSellers: LiveData<ApiState>
    fun searchSellers(query: String, dateFrom: String?, dateTo: String?) {
        sellerDataSourceFactory.search(query, dateFrom, dateTo)
        sellers.value?.dataSource?.invalidate()
    }

    val state = MediatorLiveData<ApiState>()
    val fetchError = MediatorLiveData<String>()


    /*Co-Routine*/
    private val apiJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)


    private val _customersScreen = MutableLiveData<Boolean>()
    val customersScreen: LiveData<Boolean>
        get() = _customersScreen

    init {
        _customersScreen.value = true

        /*initializing date queries*/
        dateFrom.value = null
        dateFromPersian.value = null
        dateTo.value = null
        dateToPersian.value = null

        //creating data source factory
        customerDataSourceFactory = CustomerDataSourceFactory(jwt, customerNo)
        sellerDataSourceFactory = SellerDataSourceFactory(jwt, customerNo)
        //getting the live data source from data source factory
        customersDataSource = customerDataSourceFactory.getCustomerLiveDataSource()
        sellerDataSource = sellerDataSourceFactory.getSellerLiveDataSource()
        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                /*FIXME is 10 sufficient?*/
                .setPageSize(10)
                .build()


        customers = LivePagedListBuilder(customerDataSourceFactory, pagedListConfig).build()
        sellers = LivePagedListBuilder(sellerDataSourceFactory, pagedListConfig).build()
        stateCustomers = Transformations.switchMap(customerDataSourceFactory.getCustomerLiveDataSource(), CustomerDataSource::state)
        stateSellers = Transformations.switchMap(sellerDataSourceFactory.getSellerLiveDataSource(), SellerDataSource::state)
        state.addSource(stateCustomers) {
            state.value = stateCustomers.value
        }
        state.addSource(stateSellers) {
            state.value = stateSellers.value
        }
        fetchErrorSellers = Transformations.switchMap(sellerDataSourceFactory.getSellerLiveDataSource(), SellerDataSource::fetchError)
        fetchErrorCustomers = Transformations.switchMap(customerDataSourceFactory.getCustomerLiveDataSource(), CustomerDataSource::fetchError)
        fetchError.addSource(fetchErrorCustomers) {
            fetchError.value = fetchErrorCustomers.value
        }
        fetchError.addSource(fetchErrorSellers) {
            fetchError.value = fetchErrorSellers.value
        }
        fetchOwed()
    }


    fun navigateToCustomers() {
        _customersScreen.value = true
    }

    fun navigateToSellers() {
        _customersScreen.value = false
    }

    fun query(query: String) {
        when (_customersScreen.value) {
            true -> searchCustomers(query, dateFrom.value ?: "", dateTo.value ?: "")
            else -> searchSellers(query, dateFrom.value ?: "", dateTo.value ?: "")
        }
    }


    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }
}