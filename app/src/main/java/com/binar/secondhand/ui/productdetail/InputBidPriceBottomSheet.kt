package com.binar.secondhand.ui.productdetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.androidadvance.topsnackbar.TSnackbar
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.response.BuyerProductDetailResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.databinding.BottomSheetInputBidPriceBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class InputBidPriceBottomSheet() : BottomSheetDialogFragment() {

    private val binding: BottomSheetInputBidPriceBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: ProductDetailViewModel by sharedViewModel()

    private var item: BuyerProductDetailResponse? = null

    private val args: InputBidPriceBottomSheetArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).apply {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = requireActivity().findViewById<View>(android.R.id.content)

        item = args.product

        binding.apply {
            item?.let { data ->
                data.imageUrl?.let  { imageUrl -> productImage.loadPhotoUrl(imageUrl) }
                productNameTv.text = data.name
                basePriceTv.text = "Rp ${data.basePrice?.currencyFormatter()}"

                sendButton.setOnClickListener {
                    val bidPrice = bidPriceEdt.text.toString().trim()
                    if (bidPrice.isNotBlank()) {
                        data.basePrice?.let { basePrice ->
                            if (bidPrice.toInt() < basePrice) {
                                val bidProductRequest = BidProductRequest(
                                    data.id,
                                    bidPrice.toInt()
                                )

                                lifecycleScope.launch {
                                    val result = viewModel.setBidPrice(bidProductRequest)
                                    if (result) {
                                        content.showShortSnackbar("Harga tawarmu berhasil dikirim ke penjual")
                                        val savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
                                        savedStateHandle["resultKey"] = true
                                        findNavController().popBackStack()
                                    } else {
                                        content.showShortSnackbar("Gagal menawar harga", false)
                                        findNavController().popBackStack()
                                    }
                                }
                            } else {
                                dialog?.window?.decorView?.showShortSnackbar("Harga tawar harus di bawah harga produk", false)
                            }
                        }
                    }
                }
            }
        }
    }
}