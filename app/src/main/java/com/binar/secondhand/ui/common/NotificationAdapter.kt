package com.binar.secondhand.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import com.binar.secondhand.databinding.ItemNotificationBinding
import com.binar.secondhand.utils.loadPhotoUrl

class NotificationAdapter(private val item : List<NotificationResponseItem>,val onClick : (NotificationResponseItem) -> Unit
):RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{
    class ViewHolder(val binding: ItemNotificationBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            item[position].imageUrl?.let { productImageView.loadPhotoUrl(it) }
            bidPriceTextView.text = "Ditawar Rp."+ item[position].bidPrice
            dateTextView.text = item[position].createdAt
            when(item[position].status){
                "bid" -> notificationTypeTextView.text = "Penawaran produk"
                "accepted" -> notificationTypeTextView.text = "Penawaran disetujui"

                else -> {notificationTypeTextView.text = "Penwaran Ditolak"}
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