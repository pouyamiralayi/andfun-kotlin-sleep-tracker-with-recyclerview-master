package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Customer

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    var data = listOf<Customer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: CustomerAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.customer_item_view, parent, false) as LinearLayout
        return CustomerAdapter.ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.nameView)
        val noView: TextView = itemView.findViewById(R.id.noView)
        val finView: TextView = itemView.findViewById(R.id.finView)
        val owedView: TextView = itemView.findViewById(R.id.owedView)
        val ownedView: TextView = itemView.findViewById(R.id.ownedView)
        val descView: TextView = itemView.findViewById(R.id.descView)

        fun bind(item: Customer) {
            finView.text = item.finYear
            nameView.text = item.customerName
            noView.text = item.customerNo
            descView.text = item.description
            owedView.text = item.owed
            ownedView.text = item.owned

            if (!item.owed.equals("0")) {
                owedView.setTextColor(Color.RED)
            } else {
                owedView.setTextColor(Color.GRAY)
            }

            if (!item.owned.equals("0")) {
                ownedView.setTextColor(Color.GREEN)
            } else {
                ownedView.setTextColor(Color.GRAY)
            }
        }
    }


}