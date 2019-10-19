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


    private val _customersScreen = MutableLiveData<Boolean>()
    val customersScreen: LiveData<Boolean>
        get() = _customersScreen

    fun reload(){
        when(_customersScreen.value){
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

    fun query(query:String){
        when(_customersScreen.value){
            true -> searchCustomers(query)
            else -> searchSellers(query)
        }
    }

    private fun searchSellers(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun searchCustomers(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }

    init {
        _customersScreen.value = true
        fetchCustomers()
        fetchSellers()
    }

    fun fetchCustomers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getCustomers()
            try {
                val resultList = getCustomersDeferred.await()
                customers.value = resultList
            } catch (t: Throwable) {
                fetchError.value = t.message
            }
        }
    }

    fun fetchSellers() {
        coroutineScope.launch {

            val getCustomersDeferred = StrapiApi.retrofitService.getSellers()
            try {
                val resultList = getCustomersDeferred.await()
                sellers.value = resultList
            } catch (t: Throwable) {
                fetchError.value = t.message
            }
        }
    }




    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}