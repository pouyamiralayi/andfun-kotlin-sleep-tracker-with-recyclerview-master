package com.pouyamiralayi.android.datatracker.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
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


class CustomerAdapter(val clickListener: CustomerListener, val customerNo:String, val customerName:String) : PagedListAdapter<Customer, RecyclerView.ViewHolder>(CustomerDiffCallback()) {

    var owed = "0"
        set(value) {
            field = value
            notifyItemChanged(0)
        }
    var owned = "0"
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

//    fun addHeaderAndSubmitList(list: List<Customer>, customerName: String, sellerNon:String, owed: String, owned: String) {
//        adapterScope.launch {
//            val items = when (list) {
//                null -> (listOf(DataItem.Header(customerName, sellerNon, owed, owned)))
//                else -> (listOf(DataItem.Header(customerName, sellerNon, owed, owned)) + list.map { DataItem.CustomerItem(it) })
//            }
//            withContext(Dispatchers.Main) {
//                submitList(items)
//            }
//        }
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val customerItem = getItem(position)
                customerItem?.let {
                    holder.bind(customerItem, clickListener)
                }
            }
            is TextViewHolder -> {
                holder.bind(customerName, customerNo, owed, owned)

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
        when (position){
            0 -> return ITEM_VIEW_HEADER
            else -> return ITEM_VIEW_ITEM
        }
//        return when (getItem(position)) {
//            is DataItem.Header -> ITEM_VIEW_HEADER
//            is DataItem.CustomerItem -> ITEM_VIEW_ITEM
//            else -> -1
//        }
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

class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
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