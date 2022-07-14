package com.binar.secondhand.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.MainActivity
import com.binar.secondhand.ui.common.AuthViewModel
import com.binar.secondhand.utils.RequireAuthentication
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    protected open var requireAuthentication = false

    protected open val authViewModel: AuthViewModel by sharedViewModel()

    protected open val navController: NavController by lazy { findNavController() }

    protected open fun executeRequireAuthentication() {
        return RequireAuthentication.execute(authViewModel, navController, viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }

        if (requireAuthentication) {
            executeRequireAuthentication()
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
}