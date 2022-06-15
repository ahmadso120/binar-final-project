package com.binar.secondhand.ui.account.editaccount


import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentEditAccountBinding


class EditAccountFragment : BaseFragment(R.layout.fragment_edit_account) {
 private val binding: FragmentEditAccountBinding by viewBinding()

 override var bottomNavigationViewVisibility = View.GONE

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)



 }


}