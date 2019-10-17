package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.Customer
import kotlinx.android.synthetic.main.customer_item_view.view.*

class CustomerAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<Customer>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.view.finView.text = item.finYear
        holder.view.nameView.text = item.customerName
        holder.view.noView.text = item.customerNo
        holder.view.descView.text = item.description
        holder.view.owedView.text = item.owed
        holder.view.ownedView.text = item.owned

        if(!item.owed.equals("0")){
            holder.view.owedView.setTextColor(Color.RED)
        }
        else {
            holder.view.owedView.setTextColor(Color.GRAY)
        }

        if(!item.owned.equals("0")){
            holder.view.ownedView.setTextColor(Color.GREEN)
        }
        else {
            holder.view.ownedView.setTextColor(Color.GRAY)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.customer_item_view, parent, false) as LinearLayout
        return TextItemViewHolder(view)
    }



}