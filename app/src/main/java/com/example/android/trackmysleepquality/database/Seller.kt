package com.example.android.trackmysleepquality.database

import com.squareup.moshi.Json

data class Seller(
        var id : Int = 0,
        @Json(name = "fin_year") var finYear: String = "",
        var description: String = "",
        @Json(name = "record_no") var recordNo: String = "",
        @Json(name = "expire_date") var expireDate: String = "",
        @Json(name = "seller_no") var sellerNo: String = "",
        @Json(name = "seller_name") var sellerName: String = "",
        @Json(name = "product_no") var productNo: String = "",
        @Json(name = "product_name") var product: String = "",
        @Json(name = "first_unit") var firstUnit: String = "",
        var quantity: String = "",
        var rate: String = "",
        var payment: String = "",
        var date: String = "",
        @Json(name = "created_at") var createdAt: String = "",
        @Json(name = "updated_at") var updatedAt: String = ""
)