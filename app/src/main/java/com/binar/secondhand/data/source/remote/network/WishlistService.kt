package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.WishlistRequest
import com.binar.secondhand.data.source.remote.response.WishlistResponse
import retrofit2.Response
import retrofit2.http.*

interface WishlistService {
    @POST("buyer/wishlist")
    suspend fun wishlist(@Body wishlistRequest: WishlistRequest) : Response<Unit>

    @GET("buyer/wishlist")
    suspend fun getWishlist(): Response<List<WishlistResponse>>

    @DELETE("buyer/wishlist/{id}")
    suspend fun deleteWishlist(@Path("id") id: Int): Response<Unit>
}