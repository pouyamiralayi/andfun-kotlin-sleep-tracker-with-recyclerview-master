package com.example.android.trackmysleepquality.database

data class Customer(
        var id : Int = 0,
        var finYear: String = "",
        var recordNo: String = "",
        var customerNo: String = "",
        var customerName: String = "",
        var owed: String = "",
        var owned: String = "",
        var date: String = "",
        var description: String = "",
        var createdAt: String = "",
        var updatedAt: String = ""
)