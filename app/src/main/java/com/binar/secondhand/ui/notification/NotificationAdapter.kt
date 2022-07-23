package com.binar.secondhand.ui.notification

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import com.binar.secondhand.databinding.ItemNotificationBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.dateTimeFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl

class NotificationAdapter(
    private val item: List<NotificationResponseItem>,
    private val onClick: (NotificationResponseItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isSeller = false
        if (item[position].product?.userId == item[position].user?.id) {
            isSeller = true
        }
        holder.binding.apply {
            item[position].imageUrl?.let { productImageView.loadPhotoUrl(it) }
            produkTextView.text = item[position].product?.name
            basePriceTextView.text = item[position].product?.basePrice?.currencyFormatter()
            bidPriceTextView.text = "Ditawar Rp." + item[position].bidPrice.currencyFormatter()
            dateTextView.text = item[position].createdAt.dateTimeFormatter()
            when (item[position].status) {
                "bid" -> {
                    notificationTypeTextView.text = "Penawaran produk"
                    if (isSeller) {
                        isaccetetdTextView.text = "Produkmu ditawar oleh pembeli"
                    } else {
                        isaccetetdTextView.text = "Kamu telah menawar produk"
                    }
                }
                "accepted" -> {
                    if (isSeller) {
                        notificationTypeTextView.text = "Penawaran disetujui"
                        bidPriceTextView.text =
                            "Ditawar Rp. " + item[position].bidPrice.currencyFormatter()
                        basePriceTextView.paintFlags =
                            basePriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        isaccetetdTextView.text =
                            "Anda telah menyetujui penawaran, segera hubungi pembeli"
                    } else {
                        notificationTypeTextView.text = "Penawaran disetujui"
                        bidPriceTextView.text =
                            "Berhasil Ditawar Rp. " + item[position].bidPrice.currencyFormatter()
                        basePriceTextView.paintFlags =
                            basePriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }
                "create" -> {
                    notificationTypeTextView.text = "Produk diterbitkan"
                    bidPriceTextView.visibility = View.INVISIBLE
                    isaccetetdTextView.visibility = View.GONE
                }
                "declined" -> {
                    if (isSeller) {
                        notificationTypeTextView.text = "Penawaran Ditolak "
                        bidPriceTextView.paintFlags =
                            bidPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        isaccetetdTextView.text = "Kamu telah menolak harga tawar dari pembeli"
                    } else {
                        notificationTypeTextView.text = "Penawaran Ditolak "
                        bidPriceTextView.paintFlags =
                            bidPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        isaccetetdTextView.text = "Harga tawaranmu ditolak oleh penjual"
                    }
                }
                else -> {
                    notificationTypeTextView.text = "something went wrong"
                }
            }
        }
        if (!item[position].read) {
            holder.binding.imageView2.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
           if(isSeller){
               if (item[position].status == "create"){
                   navController.navigate(NotificationFragmentDirections.actionNotificationFragmentToSellerProductFragment())
               }else{
                    navController.navigate(NotificationFragmentDirections.actionNotificationFragmentToBidderInfoFragment(item[position].orderId))
               }
           }else{
                    navController.navigate(NotificationFragmentDirections.actionNotificationFragmentToBuyerOrderFragment())
           }
            onClick.invoke(item[position])
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}