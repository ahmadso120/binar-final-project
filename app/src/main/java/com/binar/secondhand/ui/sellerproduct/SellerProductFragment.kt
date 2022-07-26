package com.binar.secondhand.ui.sellerproduct


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSellerProductBinding
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerProductFragment : BaseFragment(R.layout.fragment_seller_product) {
    private val binding: FragmentSellerProductBinding by viewBinding()

    private val viewModel by viewModel<SellerProductViewModel>()

    override var requireAuthentication = true

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
            when(it) {
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
                        binding.recyclerview.adapter = SellerProductAdapter(
                            item = sortIdDesc,
                            onCardClicked = {
                                if (it.status == "sold"){
                                    view?.showShortSnackbar("Produkmu Telah Terjual",false)
                                }else{
                                    navController.navigate(SellerProductFragmentDirections.actionSellerProductFragmentToUpdateProductFragment(it.id))
                                }
                            },
                            onDeleteClicked = {
                                deleteProduct(
                                    name = it.name,
                                    id = it.id
                                )
                            },
                            onpreviewClicked = {
                                navController.navigate(
                                    SellerProductFragmentDirections.actionSellerProductFragmentToSellerProductDetailFragment(
                                        it.id
                                    )
                                )
                            }, onShareCliked = {
                                shareProduct(it.id)
                            })
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
                            view?.showShortSnackbar("Produkmu gagal dihapus, periksa penawaran",false)
                        }
                        Result.Loading -> {

                        }
                        is Result.Success -> {
                           view?.showShortSnackbar("Produkmu berhasil dihapus")
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
    private fun shareProduct(productId: Int){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey temukan dan nego produk saya di: https://secondhand.com/product/$productId"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
}
