package com.binar.secondhand.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentRegisterBinding


class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val binding: FragmentRegisterBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE


}