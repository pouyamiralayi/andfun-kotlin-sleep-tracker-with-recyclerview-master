package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.databinding.CustomerItemViewBinding

class CustomerAdapter : ListAdapter<Customer, CustomerAdapter.ViewHolder>(CustomerDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CustomerItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Customer) {
            binding.finView.text = item.finYear
            binding.nameView.text = item.customerName
            binding.noView.text = item.customerNo
            binding.descView.text = item.description
            binding.owedView.text = item.owed
            binding.ownedView.text = item.owned

            if (!item.owed.equals("0")) {
                binding.owedView.setTextColor(Color.RED)
            } else {
                binding.owedView.setTextColor(Color.GRAY)
            }

            if (!item.owned.equals("0")) {
                binding.ownedView.setTextColor(Color.GREEN)
            } else {
                binding.ownedView.setTextColor(Color.GRAY)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomerItemViewBinding.inflate(layoutInflater, parent, false)
//                val view = layoutInflater.inflate(R.layout.customer_item_view, parent, false) as LinearLayout
                return ViewHolder(binding)
            }
        }
    }


}

class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }

}