package com.pouyamiralayi.android.datatracker.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Customer

private val customerLiveDataSource = MutableLiveData<CustomerDataSource>()

class CustomerDataSourceFactory (val jwt:String, val customerNo:String): DataSource.Factory<Int, Customer>() {
    override fun create(): DataSource<Int, Customer> {
        val customerDataSource = CustomerDataSource(jwt, customerNo)
        customerLiveDataSource.postValue(customerDataSource)
        return customerDataSource
    }

fun getCustomerLiveDataSource(): LiveData<CustomerDataSource> {
    return customerLiveDataSource
}

}
