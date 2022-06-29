package com.binar.secondhand.ui.sellerproduct

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSellerProductBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerProductFragment : BaseFragment(R.layout.fragment_seller_product) {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentSellerProductBinding by viewBinding()

    private val viewModel by viewModel<SellerProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUI()
        binding.swipeRefreshLayout.setOnRefreshListener {
            observeUI()
        }

    }

    private fun observeUI() {
        viewModel.getSellerProduct().observe(viewLifecycleOwner) {
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
                        binding.recyclerview.adapter = SellerProductAdapter(
                            item = it.data,
                            onCardClicked = {
                                Toast.makeText(
                                    requireContext(),
                                    "clicked card ${it.status}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onDeleteClicked = {
                                Toast.makeText(
                                    requireContext(),
                                    "clicked delete ${it.status}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onpreviewClicked = {
                                Toast.makeText(
                                    requireContext(),
                                    "clicked preview ${it.status}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}