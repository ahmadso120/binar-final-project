package com.binar.secondhand.ui.account.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.R
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.HistoryResponseItem
import com.binar.secondhand.databinding.ListHistoryBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.dateTimeFormatter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class HistoryAdapter(private val item: List<HistoryResponseItem>) : RecyclerView.Adapter<HistoryAdapter.MainViewHolder>() {
    class MainViewHolder(val binding: ListHistoryBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ListHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.apply {
        produkTextView.text= item[position].productName
        val url = item[position].imageUrl
        Glide.with(holder.itemView.context)
            .load(url)
            .into(productImageView)
        basePriceTextView.text = "Harga Tawar Rp. "+item[position].price.currencyFormatter()
        dateTextView.text = item[position].createdAt.dateTimeFormatter()
            when (item[position].status) {
                "accepted" -> {
                    statusTextView.text = "Diterima"
                }
                "declined" -> {
                    statusTextView.text = "Ditolak"
                    statusTextView.setTextColor(Color.parseColor("#b43b60"))
                    statusTextView.setBackgroundResource(R.color.red00)
                }
            }

    }
    }
    override fun getItemCount(): Int {
        return item.size
    }
}