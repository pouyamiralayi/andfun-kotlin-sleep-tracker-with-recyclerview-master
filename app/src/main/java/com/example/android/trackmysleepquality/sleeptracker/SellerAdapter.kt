package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Seller

class SellerAdapter: RecyclerView.Adapter<SellerAdapter.ViewHolder>() {
    var data = listOf<Seller>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: SellerAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View):RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.findViewById(R.id.nameView)
        val noView: TextView = itemView.findViewById(R.id.noView)
        val finView: TextView = itemView.findViewById(R.id.finView)
        val pNameView: TextView = itemView.findViewById(R.id.pNameView)
        val pNoView: TextView = itemView.findViewById(R.id.pNoView)
        val descView: TextView = itemView.findViewById(R.id.descView)
        val rateView: TextView = itemView.findViewById(R.id.rateView)
        val unitView: TextView = itemView.findViewById(R.id.unitView)
        val payView: TextView = itemView.findViewById(R.id.payView)
        val expireView: TextView = itemView.findViewById(R.id.expireView)
        val dateView: TextView = itemView.findViewById(R.id.dateView)
        val quantityView: TextView = itemView.findViewById(R.id.quantityView)

        fun bind(item: Seller) {
            finView.text = item.finYear
            nameView.text = item.sellerName
            noView.text = item.sellerNo
            descView.text = item.description
            pNameView.text = item.product
            pNoView.text = item.productNo
            rateView.text = item.rate
            unitView.text = item.firstUnit
            payView.text = item.payment
            dateView.text = item.date
            expireView.text = item.expireDate
            quantityView.text = item.quantity
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.customer_item_view, parent, false) as ConstraintLayout
                return ViewHolder(view)
            }
        }
    }



}