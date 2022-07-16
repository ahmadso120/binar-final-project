package com.binar.secondhand.ui.buyerorder

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.databinding.BottomReInputBidPriceBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ReInputBidPriceBottomSheet : BottomSheetDialogFragment() {
    private val binding: BottomReInputBidPriceBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: BuyerOrderViewModel by sharedViewModel()
    private var data: BuyerOrderResponse? = null
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
        data = arguments?.getParcelable("key")

        binding.apply {
            data?.product?.imageUrl?.let { productImage.loadPhotoUrl(it) }
            productNameTv.text = data?.product?.name
            basePriceTv.text = "Rp. "+data?.product?.basePrice?.currencyFormatter()
        }

    }
    companion object {
        const val TAG = "ReInputBidPriceBottomSheet"
    }
}