package com.binar.secondhand.di


import com.binar.secondhand.ui.account.accountsetting.AccountSettingViewModel
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.home.HomeViewModel
import com.binar.secondhand.ui.login.LoginViewModel
import com.binar.secondhand.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }

   
    viewModel { AccountSettingViewModel(get()) }


    viewModel { HomeViewModel(get(),get()) }

    viewModel { EditAccountViewModel(get())}

    viewModel{RegisterViewModel(get())}

}