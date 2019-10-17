package com.example.android.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.database.Seller
import com.example.android.trackmysleepquality.database.SleepNight

class SellerAdapter: RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<Seller>()

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}