package com.pouyamiralayi.android.datatracker.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pouyamiralayi.android.datatracker.R
import com.pouyamiralayi.android.datatracker.database.Seller
import com.pouyamiralayi.android.datatracker.databinding.FooterBinding
import com.pouyamiralayi.android.datatracker.databinding.HeaderBinding
import com.pouyamiralayi.android.datatracker.databinding.SellerItemViewBinding
import com.pouyamiralayi.android.datatracker.network.ApiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1
private const val ITEM_VIEW_FOOTER = 2


class SellerAdapter(val clickListener: SellerListener, val customerNo: String, val customerName: String) : PagedListAdapter<Seller, RecyclerView.ViewHolder>(SellerDiffCallback()) {

    var state: ApiState = ApiState.DONE
        set(value) {
            field = value
            notifyItemChanged(super.getItemCount())
        }
    /*these are not needed, just in case...*/
    var owed = ""
    var owned = ""
    private val adapterScope = CoroutineScope(Dispatchers.Default)


//    override fun getItemCount(): Int {
//        return super.getItemCount() + if (hasFooter()) 1 else 0
//    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == ApiState.LOADING || state == ApiState.ERROR)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val sellerItem = getItem(position)
                sellerItem?.let {
                    holder.bind(sellerItem, clickListener)
                }
            }
            is HeaderViewHolder -> {
                holder.bind(customerName, customerNo, owed, owned)

            }
            is FooterViewHolder -> {
                holder.bind(state)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_FOOTER -> FooterViewHolder.from(parent)
            else -> throw ClassCastException("unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> ITEM_VIEW_HEADER
            hasFooter() && position == super.getItemCount() - 1 -> ITEM_VIEW_FOOTER
            position < super.getItemCount() -> ITEM_VIEW_ITEM
            else -> -1
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

    class HeaderViewHolder private constructor(val binding: HeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customerName: String, customerNo: String, owed: String, owned: String) {
            binding.customerName = customerName
            binding.customerNo = customerNo
            binding.customerOwed = owed
            binding.customerOwned = owned
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class FooterViewHolder private constructor(val binding: FooterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(status: ApiState?) {
            binding.progressBar.visibility = if (status == ApiState.LOADING) VISIBLE else INVISIBLE
        }

        companion object {
            fun from(parent: ViewGroup): FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FooterBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding)
            }
        }
    }

}

class SellerDiffCallback : DiffUtil.ItemCallback<Seller>() {
    override fun areItemsTheSame(oldItem: Seller, newItem: Seller): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Seller, newItem: Seller): Boolean {
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