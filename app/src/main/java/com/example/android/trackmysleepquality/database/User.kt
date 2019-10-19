package com.example.android.trackmysleepquality.database

import com.squareup.moshi.Json

data class User(
        @Json(name = "username") val userName: String,
        val id: Int,
        val email: String,
        val provider: String,
        val confirmed: Boolean,
        val blocked: Boolean,
        val role: Role,
        @Json(name = "created_at") val createdAt: String,
        @Json(name = "updated_at") val updatedAt: String
)