package com.pouyamiralayi.android.datatracker.datatracker

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pouyamiralayi.android.datatracker.database.Customer
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DataViewModel(val customerName: String, val customerNo: String, private val jwt: String) : ViewModel() {

    /*Data Source*/
    private val customerDataSourceFactory: CustomerDataSourceFactory
    private val customersDataSource: LiveData<CustomerDataSource>
    private val sellerDataSourceFactory: SellerDataSourceFactory
    private val sellerDataSource: LiveData<SellerDataSource>

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
            else -> searchSellers("")
        }
    }

    val sellers : LiveData<PagedList<Seller>>
    fun searchSellers(query: String) {
        /*FIXME*/
        customerDataSourceFactory.search(query)
        customers.value?.dataSource?.invalidate()
    }
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

    fun query(query: String) {
        when (_customersScreen.value) {
            true -> searchCustomers(query)
            else -> searchSellers(query)
        }
    }



    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }




    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}