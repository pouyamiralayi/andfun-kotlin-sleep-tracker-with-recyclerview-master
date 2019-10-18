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

class DataViewModel : ViewModel() {

    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val customers = MutableLiveData<List<Customer>>()
    val sellers = MutableLiveData<List<Seller>>()

    val fetchError = MutableLiveData<String>()

    private val _navigateToCustomerDetail = MutableLiveData<Int>()
    val navigateToCustomerDetail: LiveData<Int>
        get() = _navigateToCustomerDetail


    private val _customersScreen = MutableLiveData<Boolean>()
    val customersScreen:LiveData<Boolean>
    get() = _customersScreen

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }

    fun getCustomers() {
        /*TODO get customers*/
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers()
            try {
                val resultList = getCustomersDeferred.await()
                customers.value = resultList
            } catch (t: Throwable) {
                fetchError.value = t.message
            }
        }
//        customers.value = listOf(
//                Customer(
//                        1, "1398", "1", "1", "test name", "2,000", "1,000",
//                        "1398/1/1", "test description", "1398/1/1", "1398/1/1"),
//                Customer(
//                        2, "1398", "2", "2", "test name 2", "2,000", "1,000",
//                        "1398/1/1", "test description 2", "1398/1/1", "1398/1/1"),
//                Customer(
//                        3, "1398", "3", "3", "test name 3", "2,000", "1,000",
//                        "1398/1/1", "test description 3", "1398/1/1", "1398/1/1"),
//                Customer(
//                        4, "1398", "4", "4", "test name 4", "2,000", "1,000",
//                        "1398/1/1", "test description 4", "1398/1/1", "1398/1/1")
//        )
    }

    fun getSellers() {
        /*TODO get sellers*/
    }

    init {
        getCustomers()
        getSellers()
    }

    fun onCustomerClick(customerId: Int) {
        _navigateToCustomerDetail.value = customerId
    }

    fun onCustomerNavigated() {
        _navigateToCustomerDetail.value = null
    }


    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}