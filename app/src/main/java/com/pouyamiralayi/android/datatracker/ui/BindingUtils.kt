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

package com.pouyamiralayi.android.datatracker.ui

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pouyamiralayi.android.datatracker.R
import com.pouyamiralayi.android.datatracker.database.Customer
import android.view.View
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.network.ApiState
import saman.zamani.persiandate.PersianDateFormat


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

//@BindingAdapter("customerOwed")
//fun TextView.setCustomerOwed(textView: TextView, item: Customer?) {
//    item?.let {
//        text = "بدهکار: " + textView.context.getString(R.string.display_price, item.owed.toFloat())
//        if (item.owed != "0") {
//            setTextColor(Color.RED)
//        } else {
//            setTextColor(Color.GRAY)
//        }
//    }
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("customerOwed")
fun TextView.setCustomerOwed(item: Customer?) {
    item?.let {
        text = "بدهکار: " + String.format("%,.0f", item.owed?.toDouble() ?: 0.0)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("customerOwned")
fun TextView.setCustomerOwned(item: Customer?) {
    item?.let {
        text = "بستانکار: " + String.format("%,.0f", item.owned?.toDouble() ?: 0.0)
    }
}

@BindingAdapter("owedColor")
fun TextView.setOwedColor(item: Customer?) {
    when {
        item?.owed == null -> setTextColor(resources.getColor(R.color.gray_text_color))
        item.owed == "0" -> setTextColor(resources.getColor(R.color.gray_text_color))
        else -> setTextColor(resources.getColor(R.color.colorRed_A400))
    }
}

@BindingAdapter("ownedColor")
fun TextView.setOwnedColor(item: Customer?) {
    if (item?.owned == null) {
        setTextColor(resources.getColor(R.color.gray_text_color))
    } else if (item.owned == "0") {
        setTextColor(resources.getColor(R.color.gray_text_color))
    } else {
        setTextColor(resources.getColor(R.color.colorGreen_A400))

    }
}

@BindingAdapter("payment")
fun TextView.setSellerPayment(item: Seller?) {
    item?.let {
        text = "پرداختی: " + String.format("%,.0f", item.payment.toDouble())
    }
}

@BindingAdapter("date")
fun TextView.setDate(date: String?) {
    date?.let {
        try {
            val splitted = date.split('T')[0]
            val persianDateFormat = PersianDateFormat("yyyy-MM-dd")
            val formatted = persianDateFormat.parseGrg(splitted)
            setText("تاریخ: " + PersianDateFormat.format(formatted, "l j F Y"))
        } catch (t: Throwable) {
            setText("تاریخ : " + "تعریف نشده است")
        }
    }
}

@BindingAdapter("exdate")
fun TextView.setExDate(date: String?) {
    date?.let{
        try {
            val splitted = date.split('T')[0]
            val persianDateFormat = PersianDateFormat("yyyy-MM-dd")
            val formatted = persianDateFormat.parseGrg(splitted)
            setText("تاریخ سررسید: " + PersianDateFormat.format(formatted, "l j F Y"))
        } catch (t: Throwable) {
            setText("تاریخ سررسید: " + "تعریف نشده است")
        }
    }
}

@BindingAdapter("bindState")
fun ImageView.setBindState(state: ApiState?) {
    state?.let{
        when (state) {
            ApiState.LOADING -> {
                visibility = View.VISIBLE
                setImageResource(R.drawable.loading_animation)
            }
            ApiState.ERROR -> {
                visibility = View.VISIBLE
                setImageResource(R.drawable.ic_connection_error)
            }

            ApiState.DONE -> {
                visibility = View.GONE
            }
    }


    }
}

@BindingAdapter("bindStateLogin")
fun ImageView.setBindStateLogin(state: ApiState) {
    when (state) {
        ApiState.LOADING -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.loading_animation)
        }
        ApiState.ERROR -> {
            visibility = View.GONE
        }

        ApiState.DONE -> {
            visibility = View.GONE
        }
    }
}