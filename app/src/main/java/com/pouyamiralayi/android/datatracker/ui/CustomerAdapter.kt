package com.pouyamiralayi.android.datatracker.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pouyamiralayi.android.datatracker.database.Customer
import com.pouyamiralayi.android.datatracker.databinding.CustomerItemViewBinding
import com.pouyamiralayi.android.datatracker.databinding.HeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1


class CustomerAdapter(val clickListener: CustomerListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(CustomerDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Customer>, customerName: String, customerNo:String, owed: String, owned: String) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header(customerName, customerNo, owed, owned))
                else -> listOf(DataItem.Header(customerName, customerNo, owed, owned)) + list.map { DataItem.CustomerItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val customerItem = getItem(position) as DataItem.CustomerItem
                holder.bind(customerItem.customer, clickListener)
            }
            is TextViewHolder -> {
                val headerItem = getItem(position) as DataItem.Header
                holder.bind(headerItem.customerName, headerItem.customerNo,headerItem.owed, headerItem.owned)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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

    class TextViewHolder private constructor(val binding: HeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customerName: String, customerNo: String, owed: String, owned: String) {
            binding.customerName = customerName
            binding.customerNo = customerNo
            binding.customerOwed = owed
            binding.customerOwned = owned
        }

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
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

    data class CustomerItem(val customer: Customer) : DataItem() {
        override val id = customer.id
    }

    data class Header(val customerName: String, val customerNo: String, val owed: String, val owned: String) : DataItem() {
        override val id = Int.MIN_VALUE
    }
}