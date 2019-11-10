package com.pouyamiralayi.android.datatracker.database

import com.squareup.moshi.Json

data class User(
        @field:Json(name = "username") val userName: String,
        val id: Int,
        val email: String,
        val provider: String,
        val confirmed: Boolean,
        val blocked: Boolean,
        val role: Role,
        @field:Json(name = "created_at") val createdAt: String,
        @field:Json(name = "updated_at") val updatedAt: String
)