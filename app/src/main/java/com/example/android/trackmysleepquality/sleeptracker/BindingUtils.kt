/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertNumericQualityToCurrency
import com.example.android.trackmysleepquality.database.Customer
import android.view.View
import com.example.android.trackmysleepquality.datatracker.DataViewModel

@BindingAdapter("customerImage")
fun ImageView.setCustomerImage(item: Customer?) {
    item?.let {
        setImageResource(when (item.owed) {
//             -> R.drawable.ic_sleep_0
            "0" -> R.drawable.ic_sleep_1
//            2 -> R.drawable.ic_sleep_2
//            3 -> R.drawable.ic_sleep_3
//            4 -> R.drawable.ic_sleep_4
//            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_5
        })
    }
}

@BindingAdapter("customerOwed")
fun TextView.setCustomerOwed(item: Customer?) {
    item?.let {
        text = convertNumericQualityToCurrency("بدهکار: " + item.owed)
        if (item.owed != "0") {
            setTextColor(Color.RED)
        } else {
            setTextColor(Color.GRAY)
        }
    }
}

@BindingAdapter("customerOwned")
fun TextView.setCustomerOwned(item: Customer?) {
    item?.let {
        text = convertNumericQualityToCurrency("بستانکار: " +item.owned)
        if (item.owned != "0") {
            setTextColor(Color.GREEN)
        } else {
            setTextColor(Color.GRAY)
        }
    }
}
