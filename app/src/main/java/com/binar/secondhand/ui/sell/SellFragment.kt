package com.binar.secondhand.ui.sell

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.SellerProductRequest
import com.binar.secondhand.data.source.remote.request.PreviewProduct
import com.binar.secondhand.data.source.remote.response.CategoryResponse
import com.binar.secondhand.databinding.FragmentSellBinding
import com.binar.secondhand.ui.camera.CameraFragment
import com.binar.secondhand.ui.common.AuthViewModel
import com.binar.secondhand.utils.*
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class SellFragment : BaseFragment(R.layout.fragment_sell) {
    override var bottomNavigationViewVisibility = View.GONE

    override var requireAuthentication = true

    private val binding: FragmentSellBinding by viewBinding()

    private val viewModel by viewModel<SellerViewModel>()

    private var categoryId: Int = 0

    private var getFile: File? = null
    private var isImageFromGallery: Boolean = false
    private var isBackCamera: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val materialToolbar: MaterialToolbar = binding.materialToolbar2
        materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        observeUI()
        setupObserver()
        binding.addPhotoBtn.setOnClickListener {
            chooseImageDialog()
        }
        binding.saveBtn.setOnClickListener {
            addSellerProduct()
        }
        binding.previewBtn.setOnClickListener {
            if (binding.productNameEdt.text.toString() != "" && binding.ProductPriceEditText.text.toString() != "" && binding.descriptionEdt.text.toString() != "" && categoryId != 0 && binding.locationEdt.text.toString() != "") {
                previewProduct()
            } else {
                Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun previewProduct() {
        val productName = binding.productNameEdt.text.toString()
        val productPrice = binding.ProductPriceEditText.text.toString().toInt()
        val productDescription = binding.descriptionEdt.text.toString()
        val category = categoryId.toString()
        val location = binding.locationEdt.text.toString()
        val previewProduct =
            PreviewProduct(
                productName = productName,
                productPrice = productPrice,
                productDescription = productDescription,
                category = category,
                location = location,
                file = getFile,
                isBackCamera = isBackCamera,
                isGalery = isImageFromGallery
            )
        logd("HARGA +> $productPrice")
        if (getFile != null) {
            logd("RESULT IS BACK CAMERA => $isBackCamera")
            logd("RESULT IS GALERY => $isImageFromGallery")
            navController.navigate(
                SellFragmentDirections.actionSellFragmentToPreviewSellFragment(
                    previewProduct
                )
            )
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun addSellerProduct() {
        binding.apply {
            val productName = binding.productNameEdt.text.toString().createPartFromString()
            val productPrice = binding.ProductPriceEditText.text.toString().createPartFromString()
            val productDescription = binding.descriptionEdt.text.toString().createPartFromString()
            val category = categoryId.toString().createPartFromString()
            val location = binding.locationEdt.text.toString().createPartFromString()

            val map = HashMap<String, RequestBody>().apply {
                put("name", productName)
                put("base_price", productPrice)
                put("description", productDescription)
                put("category_ids", category)
                put("location", location)
            }
            if (getFile != null) {
                val file = reduceFileImage(getFile as File, isBackCamera, isImageFromGallery)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                logd("img $imageMultipart")

                val sellerProductRequest = SellerProductRequest(
                    imageMultipart,
                    map
                )
                logd("acc $sellerProductRequest")
                viewModel.doAddSellerProductRequest(sellerProductRequest)
            } else {
                val sellerProductRequest = SellerProductRequest(
                    file = null,
                    map
                )
                logd("acc $sellerProductRequest")
                viewModel.doAddSellerProductRequest(sellerProductRequest)

                if (getFile != null) {
                    viewModel.doAddSellerProductRequest(sellerProductRequest)
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("choose Image")
            .setPositiveButton("Gallery") { _, _ -> startGallery() }
            .setNegativeButton("Camera") { _, _ ->
                navController.navigate(R.id.action_sellFragment_to_cameraFragment)
            }
            .show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            try {
                getFile = uriToFile(selectedImg, requireContext())
            } catch (e: Exception) {
                binding.root.showShortSnackbar("No such file or directory")
            }
            isImageFromGallery = true
            binding.addPhotoBtn.setImageURI(selectedImg)
        }
    }


    private fun setupObserver() {
        val navController = navController
        val navBackStackEntry = navController.getBackStackEntry(R.id.sellFragment)
        val window = activity?.window
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        val observerResultKey = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(CameraFragment.RESULT_KEY)
            ) {
                val result =
                    navBackStackEntry.savedStateHandle.get<Bundle>(CameraFragment.RESULT_KEY)
                val myFile = result?.getSerializable("picture") as File
                isBackCamera = result.getBoolean("isBackCamera", true)

                getFile = myFile
                isImageFromGallery = false

                val resultFile = rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
                binding.addPhotoBtn.setImageBitmap(resultFile)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observerResultKey)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observerResultKey)
            }
        })

        viewModel.category.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    val items = it.data.plus(CategoryResponse(0, "Pilih Kategori"))
                    val names = items.map { names -> names.name }
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_list, names)
                    val categoryFilterDropdownMenu =
                        binding.categoryFilterDropdownMenu.editText as? AutoCompleteTextView
                    categoryFilterDropdownMenu?.setAdapter(adapter)
                    categoryFilterDropdownMenu?.setOnItemClickListener { _, _, position, _ ->
                        val catId = items[position].id
                        categoryId = catId
                    }
                }
            }
        }
    }

    private fun observeUI() {
        viewModel.addSellerProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    navController.navigate(R.id.action_sellFragment_to_homeFragment)
                }
            }
        }
    }
}




