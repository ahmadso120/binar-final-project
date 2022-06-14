package com.binar.secondhand.di

import com.binar.secondhand.ui.home.HomeViewModel
import com.binar.secondhand.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}