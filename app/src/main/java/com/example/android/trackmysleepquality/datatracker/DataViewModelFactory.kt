package com.example.android.trackmysleepquality.datatracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DataViewModelFactory(
        private val customerName: String,
        private val customerNo: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(customerName, customerNo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
