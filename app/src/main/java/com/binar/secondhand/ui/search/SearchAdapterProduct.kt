package com.binar.secondhand.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.databinding.ListItemProductBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl

class SearchAdapterProduct (
    private var onDetailClick: (BuyerProductResponse) -> Unit
) : ListAdapter<BuyerProductResponse, SearchAdapterProduct.ProductViewHolder>(ProductDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.apply {
            product.imageUrl?.let {
                productImage.loadPhotoUrl(it)
            }
            productNameTv.text = product.name
            locationTv.text = product.location
            product.basePrice?.let {
                priceTv.text = "Rp. ${it.currencyFormatter()}"
            }
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
