package com.binar.secondhand.ui.wishlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentWishlistBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class WishlistFragment : BaseFragment(R.layout.fragment_wishlist) {
    override var bottomNavigationViewVisibility = View.GONE
    val binding: FragmentWishlistBinding by viewBinding()
    private val viewModel by viewModel<WishlistViewModel>()

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
        viewModel.getAllWishlist().observe(viewLifecycleOwner) {
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
                        binding.recyclerview.adapter = WishlistAdapter(sortIdDesc, onDeleteView = {
                            deleteWhislist(it.id,it.product.name)
                        })
                    }
                }
            }
        }
    }

    private fun deleteWhislist(id: Int, name: String?) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Hapus $name dari wishlist")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                lifecycleScope.launch {
                    val result = viewModel.deleteWishlist(id)
                    if (result) {
                        Snackbar.make(
                            binding.root,
                            "Berhasil Menghapus $name dari whislist",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        observeUI()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Gagal Menghapus $name dari whislist",
                            Snackbar.LENGTH_SHORT
                        ).show()
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
