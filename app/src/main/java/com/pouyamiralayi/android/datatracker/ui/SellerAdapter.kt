package com.pouyamiralayi.android.datatracker.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.databinding.HeaderBinding
import com.pouyamiralayi.android.datatracker.databinding.SellerItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1


class SellerAdapter(val clickListener: SellerListener) : ListAdapter<DataItemSeller, RecyclerView.ViewHolder>(SellerDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Seller>, customerName: String, customerNo: String, owed: String, owned: String) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItemSeller.Header(customerName, customerNo, owed, owned))
                else -> listOf(DataItemSeller.Header(customerName, customerNo, owed, owned)) + list.map { DataItemSeller.SellerItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val sellerItem = getItem(position) as DataItemSeller.SellerItem
                holder.bind(sellerItem.seller, clickListener)
            }
            is TextViewHolder -> {
                val headerItem = getItem(position) as DataItemSeller.Header
                holder.bind(headerItem.customerName, headerItem.customerNo, headerItem.owed, headerItem.owned)

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
            is DataItemSeller.Header -> ITEM_VIEW_HEADER
            is DataItemSeller.SellerItem -> ITEM_VIEW_ITEM
        }
    }

    class ViewHolder private constructor(val binding: SellerItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Seller, clickListener: SellerListener) {
            binding.seller = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SellerItemViewBinding.inflate(layoutInflater, parent, false)
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

class SellerDiffCallback : DiffUtil.ItemCallback<DataItemSeller>() {
    override fun areItemsTheSame(oldItem: DataItemSeller, newItem: DataItemSeller): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItemSeller, newItem: DataItemSeller): Boolean {
        return oldItem == newItem
    }

}

class SellerListener(val clickListener: (sellerId: Int) -> Unit) {
    fun onClick(seller: Seller) = clickListener(seller.id)
}

sealed class DataItemSeller {
    abstract val id: Int

    data class SellerItem(val seller: Seller) : DataItemSeller() {
        override val id = seller.id
    }

    data class Header(val customerName: String, val customerNo: String, val owed: String, val owned: String) : DataItemSeller() {
        override val id = Int.MIN_VALUE
    }
}