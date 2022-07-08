package com.binar.secondhand.ui.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.R
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.databinding.ItemSellerOrderBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.dateTimeFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl

class SellerOrderAdapter(
    private val onItemClick: (SellerOrderResponse) -> Unit
) : ListAdapter<SellerOrderResponse, SellerOrderAdapter.SellerOrderViewHolder>(SellerOrderDiffCallback) {

    class SellerOrderViewHolder(
        val binding: ItemSellerOrderBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerOrderViewHolder {
        return SellerOrderViewHolder(
            ItemSellerOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SellerOrderViewHolder, position: Int) {
        val item = getItem(position)
        val context = holder.binding.root.context
        holder.binding.apply {
            productImage.loadPhotoUrl(item.product.imageUrl)
            productNameTv.text = item.product.name
            basePriceTv.text = context.getString(
                R.string.base_price_text, item.product.basePrice.currencyFormatter()
            )
            basePriceTv.paintFlags = basePriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            bidPriceTv.text = context.getString(
                R.string.bid_price_text, item.price.currencyFormatter()
            )
            orderDateTv.text = item.updatedAt.dateTimeFormatter()
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

object SellerOrderDiffCallback : DiffUtil.ItemCallback<SellerOrderResponse>() {
    override fun areItemsTheSame(
        oldItem: SellerOrderResponse,
        newItem: SellerOrderResponse
    ): Boolean  = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: SellerOrderResponse,
        newItem: SellerOrderResponse
    ): Boolean = oldItem.id == newItem.id
}