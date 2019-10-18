package com.example.android.trackmysleepquality.sleeptracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Customer
import com.example.android.trackmysleepquality.databinding.CustomerItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1


class CustomerAdapter(val clickListener: CustomerListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(CustomerDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Customer>){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CustomerItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val customerItem = getItem(position) as DataItem.CustomerItem
                holder.bind(customerItem.customer, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_HEADER
            is DataItem.CustomerItem -> ITEM_VIEW_ITEM
        }
    }

    class ViewHolder private constructor(val binding: CustomerItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Customer, clickListener: CustomerListener) {
            binding.customer = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomerItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }


}

class CustomerDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class CustomerListener(val clickListener: (customerId: Int) -> Unit) {
    fun onClick(customer: Customer) = clickListener(customer.id)
}

sealed class DataItem {
    abstract val id: Int
    data class CustomerItem(val customer: Customer) : DataItem(){
        override val id = customer.id
    }
    object Header: DataItem(){
        override val id = Int.MIN_VALUE
    }
}