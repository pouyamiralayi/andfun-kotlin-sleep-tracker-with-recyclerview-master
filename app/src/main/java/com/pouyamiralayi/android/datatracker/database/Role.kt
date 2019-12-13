package com.pouyamiralayi.android.datatracker.database

import androidx.annotation.Keep

@Keep
data class Role(
        val id: Int,
        val name: String,
        val description: String,
        val type: String
)