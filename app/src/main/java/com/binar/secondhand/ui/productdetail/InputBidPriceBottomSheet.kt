package com.binar.secondhand.ui.productdetail

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.data.source.remote.request.BidProductRequest
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.data.source.remote.response.ProductResponse
import com.binar.secondhand.databinding.BottomSheetInputBidPriceBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.logi
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InputBidPriceBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetInputBidPriceBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: ProductDetailViewModel by sharedViewModel()

    private var item: BuyerProductResponse? = null

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
        item = arguments?.getParcelable(PRODUCT_BUNDLE)

        binding.apply {
            item?.let { data ->
                data.imageUrl?.let  { imageUrl -> productImage.loadPhotoUrl(imageUrl) }
                productNameTv.text = data.name
                basePriceTv.text = "Rp ${data.basePrice?.currencyFormatter()}"

                sendButton.setOnClickListener {
                    val bidPrice = bidPriceEdt.text.toString().trim()
                    if (bidPrice.isNotBlank()) {
                        data.basePrice?.let { basePrice ->
                            if (bidPrice < basePrice.toString()) {
                                val bidProductRequest = BidProductRequest(
                                    data.id,
                                    bidPrice.toInt()
                                )
                                lifecycleScope.launch {
                                    val result = viewModel.setBidPrice(bidProductRequest)
                                    if (result) {
                                        view.showShortSnackbar("Harga tawarmu berhasil dikirim ke penjual")
                                        val bundle = bundleOf(
                                            "id" to data.id
                                        )
                                        findNavController().navigate(R.id.productDetailFragment, bundle)
                                    } else {
                                        dialog?.window?.decorView?.showShortSnackbar("Gagal menawar harga")
                                    }

                                }

                            } else {
                                dialog?.window?.decorView?.showShortSnackbar("Harga tawar harus di bawah harga produk")
                            }
                        }

                    }
                }
            }


        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        const val PRODUCT_BUNDLE = "product_bundle"
        val TAG: String = InputBidPriceBottomSheet::class.java.simpleName
        fun newInstance(
            item: BuyerProductResponse?
        ): InputBidPriceBottomSheet {
            val args = Bundle()
            args.putParcelable(PRODUCT_BUNDLE, item)
            val fragment = InputBidPriceBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }
}