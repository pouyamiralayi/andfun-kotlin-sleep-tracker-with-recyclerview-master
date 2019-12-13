package com.pouyamiralayi.android.datatracker.database

import androidx.annotation.Keep

@Keep
data class OwedResponse(val owed:String, val owned: String, val rem: String, val plus:Boolean)