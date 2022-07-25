package com.binar.secondhand.ui.bidderinfo

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.databinding.FragmentBidderInfoBinding
import com.binar.secondhand.ui.sellerorder.SellerOrderAcceptedBottomSheet
import com.binar.secondhand.ui.sellerorder.SellerOrderViewModel
import com.binar.secondhand.ui.sellerorder.SellerOrderViewModel.UpdateStatus
import com.binar.secondhand.utils.*
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.showShortSnackbar
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BidderInfoFragment : BaseFragment(R.layout.fragment_bidder_info) {
    override var bottomNavigationViewVisibility = View.GONE

    private val binding: FragmentBidderInfoBinding by viewBinding()

    private val args: BidderInfoFragmentArgs by navArgs()

    private val viewModel: SellerOrderViewModel by sharedViewModel()

    private lateinit var builder: AlertDialog.Builder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }

        builder = AlertDialog.Builder(requireContext())

        val id = args.id
        if (id != 0) {
            viewModel.getData(id)
        }

        val idOrder = arguments?.getBundle("id")
        if (idOrder != null) {
            viewModel.getData(id)
        }
        observeUi()
    }

    private fun observeUi() {
        viewModel.getData.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {
                    view?.showShortSnackbar(it.error.toString())
                    showErrorState()
                }
                Result.Loading -> {
                    showLoadingState()
                }
                is Result.Success -> {
                    showSuccessState()
                    setupUi(it.data)
                }
            }
        }
    }

    private fun showStatusOrderBottomSheet(data: SellerOrderResponse) {
        SellerOrderAcceptedBottomSheet
            .newInstance(data)
            .show(childFragmentManager, SellerOrderAcceptedBottomSheet.TAG)
    }

    private fun setupUi(data: SellerOrderResponse?) {
        binding.apply {
            orderInfo.apply {
                data?.let {
                    when (data.status) {
                        PENDING -> {
                            contact.visibility = View.GONE
                            accept.visibility = View.VISIBLE
                            decline.visibility = View.VISIBLE
                            accept.setOnClickListener {
                                builder.setTitle("Update penawaran")
                                    .setMessage("Apakah anda ingin menerima penawaran ini?")
                                    .setCancelable(true)
                                    .setPositiveButton("Terima") { _, _ ->
                                        val statusRequestBody: RequestBody = FormBody.Builder()
                                            .add("status", ACCEPTED)
                                            .build()
                                        val state = UpdateStatus(data.id, statusRequestBody)
                                        lifecycleScope.launch {
                                            val result = viewModel.onUpdateStatusClicked(state)
                                            if (result) {
                                                view?.showShortSnackbar("Berhasil menerima penawaran")
                                                showStatusOrderBottomSheet(data)
                                            } else {
                                                view?.showShortSnackbar("Gagal menerima penawaran")
                                            }
                                        }
                                    }
                                    .setNegativeButton("Batal") { dialogInterface, _ ->
                                        dialogInterface.cancel()
                                    }
                                    .show()
                            }
                            decline.setOnClickListener {
                                builder.setTitle("Update penawaran")
                                    .setMessage("Apakah anda ingin menolak penawaran ini?")
                                    .setCancelable(true)
                                    .setPositiveButton("Tolak") { _, _ ->
                                        val statusRequestBody: RequestBody = FormBody.Builder()
                                            .add("status", DECLINED)
                                            .build()
                                        val state = UpdateStatus(data.id, statusRequestBody)
                                        lifecycleScope.launch {
                                            val result = viewModel.onUpdateStatusClicked(state)
                                            if (result) {
                                                view?.showShortSnackbar("Berhasil menolak penawaran")
                                                navController.popBackStack()
                                            } else {
                                                view?.showShortSnackbar("Gagal menolak penawaran")
                                            }
                                        }
                                    }
                                    .setNegativeButton("Batal") { dialogInterface, _ ->
                                        dialogInterface.cancel()
                                    }
                                    .show()
                            }
                        }
                        ACCEPTED -> {
                            accept.visibility = View.GONE
                            decline.visibility = View.GONE
                            contact.visibility = View.VISIBLE
                            contact.setOnClickListener {
                                data.userResponse.phoneNumber?.openWhatsApp(requireContext())
                            }
                        }
                    }
                    data.product.imageUrl?.let { it1 -> productImage.loadPhotoUrl(it1) }
                    orderDateTv.text = data.updatedAt.dateTimeFormatter()
                    productNameTv.text = data.product.name
                    basePriceTv.text = requireContext().getString(
                        R.string.base_price_text, data.product.basePrice?.currencyFormatter()
                    )
                    basePriceTv.paintFlags = basePriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    bidPriceTv.text = requireContext().getString(
                        R.string.bid_price_text, data.price.currencyFormatter()
                    )

                    userInfo.apply {
                        initialsNameTv.text = data.userResponse.fullName?.getInitialsName()
                        nameTv.text = data.userResponse.fullName
                        cityTv.text = data.userResponse.city
                    }
                }

            }
        }
    }

    private fun showSuccessState() {
        binding.contentLoadingLayout.visibility = View.GONE
        binding.contentLoadingLayout.hide()
        binding.userInfo.root.visibility = View.VISIBLE
        binding.orderInfo.root.visibility = View.VISIBLE
    }

    private fun showErrorState() {
        binding.contentLoadingLayout.visibility = View.GONE
        binding.contentLoadingLayout.hide()
        binding.userInfo.root.visibility = View.VISIBLE
        binding.orderInfo.root.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        binding.contentLoadingLayout.visibility = View.VISIBLE
        binding.contentLoadingLayout.show()
        binding.userInfo.root.visibility = View.GONE
        binding.orderInfo.root.visibility = View.GONE
    }

    companion object {
        const val ACCEPTED = "accepted"
        const val PENDING = "pending"
        const val DECLINED = "declined"
    }
}