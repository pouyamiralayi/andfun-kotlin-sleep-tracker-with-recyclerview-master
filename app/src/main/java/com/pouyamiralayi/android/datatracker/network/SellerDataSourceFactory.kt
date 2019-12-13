package com.pouyamiralayi.android.datatracker.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.pouyamiralayi.android.datatracker.database.Customer
import com.pouyamiralayi.android.datatracker.database.Seller

private val customerLiveDataSource = MutableLiveData<SellerDataSource>()

class SellerDataSourceFactory(val jwt: String, val customerNo: String) : DataSource.Factory<Int, Seller>() {
    private var dateTo: String? = null
    private var dateFrom: String? = null
    private var query = ""
    override fun create(): DataSource<Int, Seller> {
        val customerDataSource = SellerDataSource(jwt, customerNo, query, dateFrom, dateTo)
        customerLiveDataSource.postValue(customerDataSource)
        return customerDataSource
    }

    fun search(query: String, dateFrom: String?, dateTo: String?) {
        this.query = query
        this.dateFrom = dateFrom
        this.dateTo = dateTo
    }

    fun getSellerLiveDataSource(): LiveData<SellerDataSource> {
        return customerLiveDataSource
    }

}
