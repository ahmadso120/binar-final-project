package com.binar.secondhand.ui.account.accountsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentAccountSettingBinding


class AccountSettingFragment : BaseFragment(R.layout.fragment_account_setting) {
    private val binding: FragmentAccountSettingBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE
}