package com.binar.secondhand.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.local.entity.BuyerProductWithCategories
import com.binar.secondhand.databinding.ListItemProductBinding
import com.binar.secondhand.utils.ui.loadPhotoUrl

class ProductAdapter(
    private var onDetailClick: (BuyerProductWithCategories) -> Unit
) : ListAdapter<BuyerProductWithCategories, ProductAdapter.ProductViewHolder>(ProductDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.apply {
            product.buyerProductEntity.imageUrl?.let {
                productImage.loadPhotoUrl(it)
            }
            val categories = product.categories?.map {
                it.name
            }
            productNameTv.text = product.buyerProductEntity.name
            categoriesTv.text = categories?.joinToString()
            priceTv.text = "Rp. " + product.buyerProductEntity.basePrice
        }
    }

    class ProductViewHolder(
        val binding: ListItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ProductDiffCallBack: DiffUtil.ItemCallback<BuyerProductWithCategories>() {
        override fun areItemsTheSame(
            oldItem: BuyerProductWithCategories,
            newItem: BuyerProductWithCategories
        ): Boolean {
            return oldItem.buyerProductEntity.buyerProductId == newItem.buyerProductEntity.buyerProductId
        }

        override fun areContentsTheSame(
            oldItem: BuyerProductWithCategories,
            newItem: BuyerProductWithCategories
        ): Boolean {
            return oldItem == newItem
        }
    }
}