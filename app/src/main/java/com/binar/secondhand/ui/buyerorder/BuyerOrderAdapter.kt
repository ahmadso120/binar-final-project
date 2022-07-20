package com.binar.secondhand.ui.buyerorder

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.R
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.databinding.ItemBuyerOrderBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.dateTimeFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl

class BuyerOrderAdapter(
    private val item: List<BuyerOrderResponse>,
    private val onReBidClicked : (BuyerOrderResponse) -> Unit,
    private val onDeleteClicked: (BuyerOrderResponse) -> Unit,
    private val onViewClicked: (BuyerOrderResponse) -> Unit
) : RecyclerView.Adapter<BuyerOrderAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemBuyerOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBuyerOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            item[position].product?.let { it.imageUrl?.let { it1 ->
                productImageView.loadPhotoUrl(
                    it1
                )
            } }
            produkTextView.text = item[position].product?.name
            basePriceTextView.text = "Rp." + item[position].product?.basePrice?.currencyFormatter()
            bidPriceTextView.text = "Tawaranmu : Rp." + item[position].price.currencyFormatter()
            dateTextView.text = item[position].transactionDate?.dateTimeFormatter()
            when (item[position].status) {
                "accepted" -> {
                    statusTextView.text = "Diterima"
                    rebidButton.visibility = View.INVISIBLE
                    basePriceTextView.paintFlags =
                        basePriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                "pending" -> {
                    statusTextView.text = "Pending"
                    deleteButton.text = "Batalkan Order"
                    statusTextView.setTextColor(Color.BLACK)
                    statusTextView.setBackgroundResource(R.color.grey)
                    rebidButton.visibility = View.INVISIBLE
                }
                "declined" -> {
                    statusTextView.text = "Ditolak"
                    deleteButton.text = "Batalkan Order"
                    statusTextView.setTextColor(Color.parseColor("#b43b60"))
                    statusTextView.setBackgroundResource(R.color.red00)
                    bidPriceTextView.paintFlags =
                        bidPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                else -> {
                    statusTextView.text = "Diterima"
                }
            }
            deleteButton.setOnClickListener {
                onDeleteClicked.invoke(item[position])
            }
            rebidButton.setOnClickListener {
                onReBidClicked.invoke(item[position])
            }
        }
        holder.itemView.setOnClickListener {
                onViewClicked.invoke(item[position])
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
}