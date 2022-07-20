package com.binar.secondhand.ui.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.remote.response.WishlistResponse
import com.binar.secondhand.databinding.ItemWishlistBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl

class WishlistAdapter(
    val items :List<WishlistResponse>,
    val onDeleteView : (WishlistResponse) -> Unit
):RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWishlistBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            item.product.imageUrl?.let { productImageView.loadPhotoUrl(it) }
            produkTextView.text = item.product.name
            basePriceTextView. text = "Rp. "+item.product.basePrice?.currencyFormatter()
            locationTextView.text = item.product.location

            wishlistImageView.setOnClickListener {
                onDeleteView.invoke(item)
            }
        }

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(WishlistFragmentDirections.actionWishlistFragmentToProductDetailFragment(item.productId))
        }
    }

    override fun getItemCount(): Int {
      return items.size
    }
}