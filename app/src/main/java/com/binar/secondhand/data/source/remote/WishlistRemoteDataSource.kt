package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.WishlistService
import com.binar.secondhand.data.source.remote.request.WishlistRequest

class WishlistRemoteDataSource(
    private val service: WishlistService
) {
    suspend fun wishlist(wishlistRequest: WishlistRequest) = service.wishlist(wishlistRequest)

    suspend fun getWishlistByProductId() = service.getWishlist()

    suspend fun deleteWishlist(id: Int) = service.deleteWishlist(id)

    suspend fun getAllWishList() = service.getWishlist()
}