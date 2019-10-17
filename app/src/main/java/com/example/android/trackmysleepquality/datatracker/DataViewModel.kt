package com.example.android.trackmysleepquality.datatracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller

class DataViewModel:ViewModel() {

    val customers = MutableLiveData<List<Customer>>()
    val sellers = MutableLiveData<List<Seller>>()

    fun getCustomers(){

    }

    fun getSellers(){

    }

    init {
        getCustomers()
        getSellers()
    }
}