package com.binar.secondhand.ui.buyerorder

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.data.source.remote.response.ProductResponse
import com.binar.secondhand.databinding.FragmentBuyerOrderBinding
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class BuyerOrderFragment : BaseFragment(R.layout.fragment_buyer_order) {
    override var bottomNavigationViewVisibility = View.GONE
    private val binding: FragmentBuyerOrderBinding by viewBinding()
    private val viewModel by viewModel<BuyerOrderViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: MaterialToolbar = binding.materialToolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        observeUI()
        binding.swipeRefreshLayout.setOnRefreshListener {
            observeUI()
        }
    }

    private fun observeUI() {
        viewModel.getAllOrder().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
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
                            recyclerview.visibility = View.GONE
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
                        binding.recyclerview.adapter = BuyerOrderAdapter(sortIdDesc, onDeleteClicked = { order ->
                            deleteOrder(order.id, order.product?.name)
                        }, onReBidClicked = {
                            setupBottomSheet(it)
                        }, onViewClicked = {
                            findNavController().navigate(BuyerOrderFragmentDirections.actionBuyerOrderFragmentToProductDetailFragment(id = it.productId))
                        })
                    }
                }
            }
        }
    }

    private fun deleteOrder(id: Int, name: String?) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Hapus/Batalkan Order $name Kamu?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                viewModel.deleteOrder(id).observe(viewLifecycleOwner) {
                    when (it) {
                        is Result.Error -> {
                      view?.showShortSnackbar("Order gagal dihapus",false)
                        }
                        Result.Loading -> {

                        }
                        is Result.Success -> {
                         view?.showShortSnackbar("Order kamu berhasil dihapus")
                            observeUI()
                        }
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()

    }

    private fun setupBottomSheet(data : BuyerOrderResponse){
        val bottomSheet = ReInputBidPriceBottomSheet()
        bottomSheet.show(parentFragmentManager,ReInputBidPriceBottomSheet.TAG)
        val bundle = Bundle()
        bundle.putParcelable("key", data)
        bottomSheet.arguments = bundle
    }
}