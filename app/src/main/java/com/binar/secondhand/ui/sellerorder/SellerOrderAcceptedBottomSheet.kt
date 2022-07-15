package com.binar.secondhand.ui.sellerorder

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.data.source.remote.response.SellerOrderResponse
import com.binar.secondhand.databinding.BottomSheetSellerOrderAcceptedBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.getInitialsName
import com.binar.secondhand.utils.openWhatsApp
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SellerOrderAcceptedBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetSellerOrderAcceptedBinding by viewBinding(CreateMethod.INFLATE)

    private var item: SellerOrderResponse? = null
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
        item = arguments?.getParcelable(ORDER_BUNDLE)

        setupUi(item)
    }

    private fun setupUi(item: SellerOrderResponse?) {
        binding.apply {
            item?.let { data ->
                initialsNameTv.text = data.userResponse.fullName.getInitialsName()
                nameTv.text = data.userResponse.fullName
                cityTv.text = data.userResponse.city
                data.product.imageUrl?.let { productImage.loadPhotoUrl(it) }
                productNameTv.text = data.product.name
                basePriceTv.text = requireContext().getString(
                    R.string.base_price_text, data.product.basePrice?.currencyFormatter()
                )
                basePriceTv.paintFlags = basePriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                bidPriceTv.text = requireContext().getString(
                    R.string.bid_price_text, data.price.currencyFormatter()
                )
                contact.setOnClickListener {
                    data.userResponse.phoneNumber.openWhatsApp(requireContext())
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val bundle = bundleOf("id" to item?.id)
        findNavController().navigate(R.id.bidderInfoFragment, bundle)
    }

    companion object {
        const val ORDER_BUNDLE = "order_bundle"
        val TAG: String = SellerOrderAcceptedBottomSheet::class.java.simpleName
        fun newInstance(
            item: SellerOrderResponse?
        ): SellerOrderAcceptedBottomSheet {
            val args = Bundle()
            args.putParcelable(ORDER_BUNDLE, item)
            val fragment = SellerOrderAcceptedBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }
}