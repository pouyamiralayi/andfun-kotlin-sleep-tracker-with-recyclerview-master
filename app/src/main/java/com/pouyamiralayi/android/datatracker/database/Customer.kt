package com.pouyamiralayi.android.datatracker.database

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class Customer(
        var id : Int = 0,
        @field:Json(name = "fin_year") var finYear: String = "",
        @field:Json(name = "record_no") var recordNo: String = "",
        @field:Json(name = "customer_no") var customerNo: String = "",
        @field:Json(name = "customer_name") var customerName: String = "",
        var owed: String? = "",
        var owned: String? = "",
        var date: String? = "",
        var description: String = "",
        @field:Json(name = "created_at") var createdAt: String = "",
        @field:Json(name = "updated_at") var updatedAt: String = ""
)