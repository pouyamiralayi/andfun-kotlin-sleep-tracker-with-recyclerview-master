package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Customer

class CustomerAdapter : ListAdapter<Customer,CustomerAdapter.ViewHolder>(CustomerDiffCallback()) {



    override fun onBindViewHolder(holder: CustomerAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        companion object {
             fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.customer_item_view, parent, false) as LinearLayout
                return ViewHolder(view)
            }
        }
    }



}

class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>(){
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }

}