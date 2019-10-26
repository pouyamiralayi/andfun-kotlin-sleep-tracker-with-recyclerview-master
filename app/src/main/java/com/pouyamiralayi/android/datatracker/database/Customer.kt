package com.pouyamiralayi.android.datatracker.database

import com.squareup.moshi.Json

data class Customer(
        var id : Int = 0,
        @Json(name = "fin_year") var finYear: String = "",
        @Json(name = "record_no") var recordNo: String = "",
        @Json(name = "customer_no") var customerNo: String = "",
        @Json(name = "customer_name") var customerName: String = "",
        var owed: String? = "",
        var owned: String? = "",
        var date: String? = "",
        var description: String = "",
        @Json(name = "created_at") var createdAt: String = "",
        @Json(name = "updated_at") var updatedAt: String = ""
)