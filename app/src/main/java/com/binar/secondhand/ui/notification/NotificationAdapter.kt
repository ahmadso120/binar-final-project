package com.binar.secondhand.ui.notification

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import com.binar.secondhand.databinding.ItemNotificationBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl
import java.text.SimpleDateFormat

class NotificationAdapter(private val item : List<NotificationResponseItem>,
                          private val onClick : (NotificationResponseItem) -> Unit
):RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{
    class ViewHolder(val binding: ItemNotificationBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            item[position].imageUrl?.let { productImageView.loadPhotoUrl(it) }
            bidPriceTextView.text = "Ditawar Rp."+ item[position].bidPrice.currencyFormatter()
            val inputTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputTimeFormat = SimpleDateFormat("dd MMM, HH:mm")
            val date = inputTimeFormat.parse(item[position].createdAt)
            val dateFormat = outputTimeFormat.format(date)
            dateTextView.text = dateFormat
            when(item[position].status){
                "bid" -> notificationTypeTextView.text = "Penawaran produk"
                "accepted" ->{
                    notificationTypeTextView.text = "Penawaran disetujui"
                    bidPriceTextView.text= "Berhasil Ditawar Rp. "+ item[position].bidPrice.currencyFormatter()
                    basePriceTextView.paintFlags= basePriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isaccetetdTextView.visibility = View.VISIBLE
                }

                else -> {
                    notificationTypeTextView.text = "Penwaran Ditolak"
                    bidPriceTextView.paintFlags= basePriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
        if (!item[position].read){
            holder.binding.imageView2.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            onClick.invoke(item[position])
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}