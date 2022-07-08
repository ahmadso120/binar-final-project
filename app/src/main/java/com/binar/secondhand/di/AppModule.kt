package com.binar.secondhand.di


import com.binar.secondhand.ui.account.accountsetting.AccountSettingViewModel
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.common.AuthViewModel
import com.binar.secondhand.ui.common.ConnectionViewModel
import com.binar.secondhand.ui.home.HomeViewModel
import com.binar.secondhand.ui.login.LoginViewModel
import com.binar.secondhand.ui.notification.NotificationViewModel
import com.binar.secondhand.ui.productdetail.ProductDetailViewModel
import com.binar.secondhand.ui.register.RegisterViewModel

import com.binar.secondhand.ui.search.SearchViewModel

import com.binar.secondhand.ui.sellerorder.SellerOrderViewModel
import com.binar.secondhand.ui.sell.SellerViewModel
import com.binar.secondhand.ui.sellerproduct.SellerProductViewModel
import com.binar.secondhand.ui.sellerproduct.detail.SellerProductDetailViewModel
import com.binar.secondhand.ui.sellerproduct.updateproduct.UpdateProductViewmodel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }

    viewModel { AccountSettingViewModel(get()) }

    viewModel { HomeViewModel(get(),get()) }

    viewModel { EditAccountViewModel(get())}

    viewModel { RegisterViewModel(get()) }

    viewModel { ConnectionViewModel(androidContext()) }

    viewModel { NotificationViewModel(get()) }
    
    viewModel {SellerViewModel(get(),get())}

    viewModel { AuthViewModel(get()) }

    viewModel { SellerOrderViewModel(get()) }

    viewModel { SellerProductViewModel(get()) }

    viewModel { SellerProductDetailViewModel(get(),get()) }

    viewModel { UpdateProductViewmodel(get(),get()) }


    viewModel {NotificationViewModel(get())}
    viewModel {SearchViewModel(get())}

    viewModel { ProductDetailViewModel(get(), get()) }

}