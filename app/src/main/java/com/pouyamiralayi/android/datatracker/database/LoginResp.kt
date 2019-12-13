package com.pouyamiralayi.android.datatracker.database

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class LoginResp(
        @field:Json(name = "jwt") val jwt: String,
        @field:Json(name = "user") val user: User
)
