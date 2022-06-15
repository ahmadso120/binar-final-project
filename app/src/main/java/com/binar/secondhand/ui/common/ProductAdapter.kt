package com.binar.secondhand.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.databinding.ListItemProductBinding
import com.binar.secondhand.utils.loadPhotoUrl
import java.text.NumberFormat
import java.util.*

class ProductAdapter(
    private var onDetailClick: (BuyerProductResponse) -> Unit
) : ListAdapter<BuyerProductResponse, ProductAdapter.ProductViewHolder>(ProductDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.apply {
            product.imageUrl?.let {
                productImage.loadPhotoUrl(product.imageUrl)
            }
            val categories = product.Categories?.map {
                it.name
            }
            productNameTv.text = product.name
            categoriesTv.text = categories?.joinToString()
            priceTv.text = "Rp. " + NumberFormat.getNumberInstance(Locale.US).format(product.basePrice)
        }
    }

    class ProductViewHolder(
        val binding: ListItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ProductDiffCallBack: DiffUtil.ItemCallback<BuyerProductResponse>() {
        override fun areItemsTheSame(
            oldItem: BuyerProductResponse,
            newItem: BuyerProductResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BuyerProductResponse,
            newItem: BuyerProductResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}