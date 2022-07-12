package com.binar.secondhand.ui.sell.previewsellproduct

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.SellerProductRequest
import com.binar.secondhand.databinding.FragmentPreviewSellBinding
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.sell.SellerViewModel
import com.binar.secondhand.utils.createPartFromString
import com.binar.secondhand.utils.logd
import com.binar.secondhand.utils.reduceFileImage
import com.binar.secondhand.utils.rotateBitmap
import com.binar.secondhand.utils.ui.loadPhotoUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PreviewSellFragment : BaseFragment(R.layout.fragment_preview_sell) {
    override var bottomNavigationViewVisibility = View.GONE
    private val binding: FragmentPreviewSellBinding by viewBinding()
    private val viewModelProduct by viewModel<SellerViewModel>()
    private val viewModelSeller by viewModel<EditAccountViewModel>()
    private val arguments : PreviewSellFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logd("PreviewFragment : $arguments")
        logd("RESULT IS BACK CAMERA PREVIEW=> ${arguments.previewProduct.isBackCamera}")
        logd("RESULT IS GALERY PREVIEW=> ${arguments.previewProduct.isGalery}")
        binding.saveBtn.setOnClickListener {
            addProduct()
        }
        setUpObserver()
        observerUi()


    }
    private fun setUpObserver(){
        viewModelSeller.getAccount().observe(viewLifecycleOwner){
            logd("PreviewFragment : $it")
            when(it){
                is Result.Error -> {
                    Toast.makeText(requireContext(),"Kamu telah mengupload 5 barang :)" , Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {}

                is Result.Success -> {
                    binding.apply {
                        brandNameTextView.text = arguments.previewProduct.productName
                        brandCategoriesTextView.text = arguments.previewProduct.category
                        brandPriceTextView.text = arguments.previewProduct.productPrice.toString()
                        descriptionTextView.text = arguments.previewProduct.productDescription
                        sellerNameTextView.text = it.data.fullName
                        sellerCityTextView.text = it.data.city
                        it.data.imageUrl?.let { it1 -> imageSeller.loadPhotoUrl(it1) }
//                        val resultFile = BitmapFactory.decodeFile(arguments.previewProduct?.file?.path)

                        if(arguments.previewProduct.isGalery){
                            val resultFile = BitmapFactory.decodeFile(arguments.previewProduct.file?.path)
                            imgPhoto.setImageBitmap(resultFile)

                        }else{
                            val resultFile = rotateBitmap(
                                BitmapFactory.decodeFile(arguments.previewProduct.file?.path),arguments.previewProduct.isBackCamera
                            )
                            imgPhoto.setImageBitmap(resultFile)
                        }

                    }
                }
            }
        }
    }
    private fun observerUi(){
        viewModelProduct.addSellerProduct.observe(viewLifecycleOwner){
            when(it){
                is Result.Error -> {}
                Result.Loading -> {
                }
                is Result.Success -> {
                    findNavController().navigate(R.id.action_previewSellFragment_to_homeFragment)
                }
            }
        }
    }
    private fun addProduct(){
        val productName = arguments.previewProduct.productName.createPartFromString()
        val productPrice = arguments.previewProduct.productPrice.toString().createPartFromString()
        val productDescription = arguments.previewProduct.productDescription.createPartFromString()
        val category = arguments.previewProduct.category.createPartFromString()
        val location = arguments.previewProduct.location.createPartFromString()

        val map = HashMap<String, RequestBody>().apply {
            put("name", productName)
            put("base_price",productPrice)
            put("description", productDescription)
            put("category_ids", category)
            put("location", location)
        }
        val files = arguments.previewProduct.file
        val file = reduceFileImage(files as File, arguments.previewProduct.isBackCamera,
            arguments.previewProduct.isGalery)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
        val addSellerProductRequest = SellerProductRequest(
            file = imageMultipart,
            map
        )
        viewModelProduct.doAddSellerProductRequest(addSellerProductRequest)
    }
}