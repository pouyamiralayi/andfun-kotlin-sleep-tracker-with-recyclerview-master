package com.pouyamiralayi.android.datatracker.database

import com.squareup.moshi.Json

data class Seller(
        var id : Int = 0,
        @field:Json(name = "fin_year") var finYear: String = "",
        var description: String = "",
        @field:Json(name = "record_no") var recordNo: String = "",
        @field:Json(name = "expire_date") var expireDate: String? = "",
        @field:Json(name = "seller_no") var sellerNo: String = "",
        @field:Json(name = "seller_name") var sellerName: String = "",
        @field:Json(name = "product_no") var productNo: String = "",
        var product: String = "",
        @field:Json(name = "first_unit") var firstUnit: String = "",
        var quantity: String = "",
        var rate: String = "",
        var payment: String = "",
        var date: String? = "",
        @field:Json(name = "created_at") var createdAt: String = "",
        @field:Json(name = "updated_at") var updatedAt: String = ""
)