package com.binar.secondhand.ui.sellerproduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.SellerProductResponse
import com.binar.secondhand.databinding.ItemSellerProductBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl
import java.text.SimpleDateFormat

class SellerProductAdapter(
    private val item: List<SellerProductResponse>,
    private val onCardClicked : (SellerProductResponse) -> Unit,
    private val onpreviewClicked : (SellerProductResponse) -> Unit,
    private val onDeleteClicked : (SellerProductResponse) -> Unit
) : RecyclerView.Adapter<SellerProductAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemSellerProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSellerProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            item[position].imageUrl?.let { productImageView.loadPhotoUrl(it) }
            produkTextView.text = item[position].name
            basePriceTextView.text = "Rp. " + item[position].basePrice.currencyFormatter()
            val inputTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputTimeFormat = SimpleDateFormat("dd MMM, HH:mm")
            val date = inputTimeFormat.parse(item[position].createdAt)
            val dateFormat = outputTimeFormat.format(date)
            dateTextView.text = dateFormat

            itemCardView.setOnClickListener {
                onCardClicked.invoke(item[position])
            }

            deleteButton.setOnClickListener {
                onDeleteClicked.invoke(item[position])
            }

            previewButton.setOnClickListener {
                onpreviewClicked.invoke(item[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
}