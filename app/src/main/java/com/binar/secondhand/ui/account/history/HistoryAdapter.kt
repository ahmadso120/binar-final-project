package com.binar.secondhand.ui.account.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.HistoryResponseItem
import com.binar.secondhand.databinding.ListHistoryBinding
import com.binar.secondhand.utils.currencyFormatter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class HistoryAdapter(private val item: List<HistoryResponseItem>) : RecyclerView.Adapter<HistoryAdapter.MainViewHolder>() {
    class MainViewHolder(val binding: ListHistoryBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ListHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.produkTextView.text= item[position].productName
        val url = item[position].imageUrl
        Glide.with(holder.itemView.context)
            .load(url)
            .into(holder.binding.productImageView)
        holder.binding.basePriceTextView.text = "Rp. "+item[position].price.currencyFormatter()
        val inputTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputTimeFormat = SimpleDateFormat("dd MMM, HH:mm")
        val date = inputTimeFormat.parse(item[position].createdAt)
        val dateFormat = date?.let { outputTimeFormat.format(it) }
        holder.binding.dateTextView.text = dateFormat
//        holder.itemView.setOnClickListener {
//            val movie = Result(
//                posterPath = item[position].posterPath,
//                title = item[position].title,
//                overview = item[position].overview,
//                releaseDate = item[position].releaseDate,
//                adult = item[position].adult
//            )
//            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRecomendationFragment(movie))
//        }
    }
    override fun getItemCount(): Int {
        return item.size
    }
}