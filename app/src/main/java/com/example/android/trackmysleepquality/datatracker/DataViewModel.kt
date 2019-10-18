package com.example.android.trackmysleepquality.datatracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.example.android.trackmysleepquality.network.StrapiApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel : ViewModel() {

    val customers = MutableLiveData<List<Customer>>()
    val sellers = MutableLiveData<List<Seller>>()
    private val _navigateToCustomerDetail = MutableLiveData<Int>()
    val navigateToCustomerDetail: LiveData<Int>
        get() = _navigateToCustomerDetail


    fun getCustomers() {
        /*TODO get customers*/
//        StrapiApi.retrofitService.getCustomers().enqueue(object: Callback<Customer> {
//            override fun onFailure(call: Call<Customer>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })
        customers.value = listOf(
                Customer(
                1, "1398","1","1","test name", "2,000","1,000",
                "1398/1/1","test description","1398/1/1","1398/1/1"),
                Customer(
                        2, "1398","2","2","test name 2", "2,000","1,000",
                        "1398/1/1","test description 2","1398/1/1","1398/1/1"),
                Customer(
                        3, "1398","3","3","test name 3", "2,000","1,000",
                        "1398/1/1","test description 3","1398/1/1","1398/1/1"),
                Customer(
                        4, "1398","4","4","test name 4", "2,000","1,000",
                        "1398/1/1","test description 4","1398/1/1","1398/1/1")
        )
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

    fun onCustomerNavigated(){
        _navigateToCustomerDetail.value = null
    }


    /*TODO*/
//    fun getCustomerById(customerId: Int){
//
//    }


}