package com.binar.secondhand.ui.buyerorder

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.RebidBuyerOrderRequest
import com.binar.secondhand.data.source.remote.response.BuyerOrderResponse
import com.binar.secondhand.databinding.BottomReInputBidPriceBinding
import com.binar.secondhand.utils.currencyFormatter
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
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
            sendButton.setOnClickListener {
                if (bidPriceEdt.text.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Masukan harga nego kamu",Toast.LENGTH_SHORT).show()
                }else if(bidPriceEdt.text.toString().toInt() > data?.basePrice.toString().toInt()){
                    Toast.makeText(requireContext(),"Harga nego melebihi harga barang",Toast.LENGTH_SHORT).show()
                }
                else {
                    doUpdateBidPrice(data?.id)
                }
            }
        }

    }
    private fun doUpdateBidPrice(id: Int?){
        val price = RebidBuyerOrderRequest(
            bidPrice = binding.bidPriceEdt.text.toString().toInt()
        )
        logd("id => ${data?.id}")
        if (id != null) {
            viewModel.updateBidPrice(id,price).observe(viewLifecycleOwner){
                when(it){
                    is Result.Error -> {
                        this.dismiss()
                        parentFragment?.view?.let { it1 -> Snackbar.make(it1,"Nego Ulang Gagal",Snackbar.LENGTH_SHORT).show() }
                    }
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        this.dismiss()
                        parentFragment?.view?.let { it1 -> Snackbar.make(it1,"Nego Ulang Berhasil",Snackbar.LENGTH_SHORT).show() }
                    }

                }
            }
        }

    }

    companion object {
        const val TAG = "ReInputBidPriceBottomSheet"
    }
}