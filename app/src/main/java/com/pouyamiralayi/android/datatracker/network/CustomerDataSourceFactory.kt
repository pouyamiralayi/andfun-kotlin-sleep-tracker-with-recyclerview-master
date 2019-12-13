package com.pouyamiralayi.android.datatracker.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Customer

private val customerLiveDataSource = MutableLiveData<CustomerDataSource>()

class CustomerDataSourceFactory(val jwt: String, val customerNo: String) : DataSource.Factory<Int, Customer>() {
    private var query = ""
    private var dateFrom: String? = null
    private var dateTo: String?  = null
    override fun create(): DataSource<Int, Customer> {
        val customerDataSource = CustomerDataSource(jwt, customerNo, query, dateFrom, dateTo)
        customerLiveDataSource.postValue(customerDataSource)
        return customerDataSource
    }

    fun search(query: String, dateFrom:String?, dateTo:String?) {
        this.query = query
        this.dateFrom = dateFrom
        this.dateTo = dateTo
    }

    fun getCustomerLiveDataSource(): LiveData<CustomerDataSource> {
        return customerLiveDataSource
    }

}
