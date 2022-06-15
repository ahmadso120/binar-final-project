package com.binar.secondhand.di

import com.binar.secondhand.ui.home.HomeViewModel
import com.binar.secondhand.ui.login.LoginViewModel
import com.binar.secondhand.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel{RegisterViewModel(get())}
}