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
import com.example.android.trackmysleepquality.database.Seller
import com.example.android.trackmysleepquality.databinding.SellerItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_HEADER = 0
private const val ITEM_VIEW_ITEM = 1


class SellerAdapter(val clickListener: SellerListener) : ListAdapter<DataItemSeller, RecyclerView.ViewHolder>(SellerDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Seller>) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItemSeller.Header)
                else -> listOf(DataItemSeller.Header) + list.map { DataItemSeller.SellerItem(it) }
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

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
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

    object Header : DataItemSeller() {
        override val id = Int.MIN_VALUE
    }
}