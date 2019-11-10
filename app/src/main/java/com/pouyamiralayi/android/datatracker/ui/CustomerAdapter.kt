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
import com.pouyamiralayi.android.datatracker.network.ApiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1
private const val ITEM_VIEW_FOOTER = 2


class CustomerAdapter(val clickListener: CustomerListener, val customerNo: String, val customerName: String) : PagedListAdapter<Customer, RecyclerView.ViewHolder>(CustomerDiffCallback()) {

    var state: ApiState = ApiState.DONE
        set(value) {
            field = value
            notifyDataSetChanged()
//            notifyItemChanged(super.getItemCount())
        }

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

    var rem = "0"
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    var plus = true
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 1 && (state == ApiState.LOADING || state == ApiState.ERROR)
    }


    private val adapterScope = CoroutineScope(Dispatchers.Default)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val customerItem = getItem(position - 1)
                customerItem?.let {
                    holder.bind(customerItem, clickListener)
                }
            }
            is TextViewHolder -> {
                holder.bind(customerName, customerNo, owed, owned, rem, plus)

            }
            is SellerAdapter.FooterViewHolder -> {
                holder.bind(state)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_FOOTER -> SellerAdapter.FooterViewHolder.from(parent)
            else -> throw ClassCastException("unknown view type $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> ITEM_VIEW_HEADER
            hasFooter() && position == getItemCount() - 2 -> ITEM_VIEW_FOOTER
            else -> ITEM_VIEW_ITEM
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
        fun bind(customerName: String, customerNo: String, owed: String, owned: String, rem: String, plus:Boolean) {
            binding.customerName = customerName
            binding.customerNo = customerNo
            binding.customerOwed = owed
            binding.customerOwned = owned
            binding.customerRem = rem
            binding.plus = plus
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