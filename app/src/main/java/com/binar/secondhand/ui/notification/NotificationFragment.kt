package com.binar.secondhand.ui.notification


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentNotificationBinding
import com.binar.secondhand.storage.AppLocalData
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.divider.MaterialDividerItemDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationFragment : BaseFragment(R.layout.fragment_notification) {
    private val binding: FragmentNotificationBinding by viewBinding()

    private val viewModel by viewModel<NotificationViewModel>()

    override var bottomNavigationViewVisibility = View.GONE

    override var requireAuthentication = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = binding.materialToolbar2
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        observeUI()
        binding.swipeRefreshLayout.setOnRefreshListener {
            observeUI()
        }
    }

    private fun observeUI() {
        viewModel.getAllNotification().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.contentLoadingLayout.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (it.data.isEmpty()) {
                        binding.apply {
                            noItemImageView.visibility = View.VISIBLE
                            noItemTextView.visibility = View.VISIBLE
                            chckBackTextView.visibility = View.VISIBLE
                        }
                    } else {
                        val layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.recyclerview.layoutManager = layoutManager
                        val sortIdDesc = it.data.sortedWith(compareBy {
                            it.id
                        }).reversed()
                        binding.recyclerview.adapter = NotificationAdapter(sortIdDesc) { item ->
                            viewModel.doPatchNotification(item.id)
                        }
                        val divider = MaterialDividerItemDecoration(
                            requireContext(),
                            layoutManager.orientation
                        )
                        divider.dividerInsetStart = 40
                        divider.dividerInsetEnd = 40
                        binding.recyclerview.addItemDecoration(divider)
                    }

                }
            }
        }
        viewModel.patchNotification.observe(viewLifecycleOwner) { item ->
            when (item) {
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
                Result.Loading -> {

                }
                is Result.Success -> {

                }
            }

        }
    }
}