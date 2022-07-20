package com.binar.secondhand.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.WishlistRepository
import com.binar.secondhand.data.source.remote.response.WishlistResponse

class WishlistViewModel(private val repository: WishlistRepository): ViewModel() {

    fun getAllWishlist(): LiveData<Result<List<WishlistResponse>>>{
        return repository.getAllWishList()
    }

    suspend fun deleteWishlist(id: Int): Boolean{
        return repository.deleteWishlist(id)
    }
}