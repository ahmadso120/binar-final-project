package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.SellerCategoryService

class SellerCategoryDataSource (
    private val sellerCategoryService: SellerCategoryService
) {
    suspend fun getCategories() = sellerCategoryService.getCategories()
}