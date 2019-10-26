package com.pouyamiralayi.android.datatracker.datatracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DataViewModelFactory(
        private val customerName: String,
        private val customerNo: String,
        private val jwt: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(customerName, customerNo, jwt) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
