package com.binar.secondhand.ui.notification


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentNotificationBinding
import com.binar.secondhand.ui.common.NotificationAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.divider.MaterialDividerItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationFragment : BaseFragment(R.layout.fragment_notification) {
    private val binding: FragmentNotificationBinding by viewBinding()
    private val viewModel by viewModel<NotificationViewModel>()
    override var bottomNavigationViewVisibility = View.GONE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = binding.materialToolbar2
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        observeUI()
    }

    private fun observeUI(){
        viewModel.getAllNotification().observe(viewLifecycleOwner){
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    binding.recyclerview.layoutManager = layoutManager
                    binding.recyclerview.adapter= NotificationAdapter(it.data){
                        Toast.makeText(requireContext(),"clicked ${it.id}",Toast.LENGTH_SHORT).show()
                    }
                    val divider = MaterialDividerItemDecoration(requireContext(), layoutManager.orientation)
                    divider.dividerInsetStart = 32
                    divider.dividerInsetEnd = 32
                    binding.recyclerview.addItemDecoration(divider)

                }
            }
        }
    }
}