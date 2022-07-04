package com.binar.secondhand.ui.sellerproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSellerProductBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerProductFragment : BaseFragment(R.layout.fragment_seller_product) {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val binding: FragmentSellerProductBinding by viewBinding()

    private val viewModel by viewModel<SellerProductViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
                            item = sortIdDesc,
                            onCardClicked = {
                                Toast.makeText(
                                    requireContext(),
                                    "clicked card ${it.status}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onDeleteClicked = {
                                deleteProduct(
                                    name = it.name,
                                    id = it.id
                                )
                            },
                            onpreviewClicked = {
                              findNavController().navigate(SellerProductFragmentDirections.actionSellerProductFragmentToSellerProductDetailFragment(it.id))
                            }
                        )
                    }
                }
            }
        }

    }


    private fun deleteProduct(name: String, id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Hapus ${name} dari daftar jual ?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                viewModel.doDeleteProduct(id).observe(viewLifecycleOwner) {
                    when (it) {
                        is Result.Error -> {
                            Snackbar.make(
                                binding.root,
                                "Produkmu gagal dihapus: ${it.error}",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        Result.Loading -> {}
                        is Result.Success -> {
                            Snackbar.make(
                                binding.root,
                                "Produkmu berhasil dihapus",
                                Snackbar.LENGTH_LONG
                            ).show()
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
}
