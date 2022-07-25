package com.binar.secondhand.ui.sell

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log


import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.SellerProductRequest
import com.binar.secondhand.data.source.remote.request.PreviewProduct
import com.binar.secondhand.data.source.remote.response.CategoryResponse
import com.binar.secondhand.databinding.FragmentSellBinding
import com.binar.secondhand.ui.account.editaccount.EditAccountViewModel
import com.binar.secondhand.ui.camera.CameraFragment
import com.binar.secondhand.utils.*
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class SellFragment : BaseFragment(R.layout.fragment_sell) {
    override var bottomNavigationViewVisibility = View.GONE

    override var requireAuthentication = true
    private val categoryID = ArrayList<Int>()
    private val binding: FragmentSellBinding by viewBinding()
    private val categoryName = ArrayList<String>()
    private val viewModel by viewModel<SellerViewModel>()
    private val viewModelSeller by viewModel<EditAccountViewModel>()
    private var getFile: File? = null
    private var isImageFromGallery: Boolean = true
    private var isBackCamera: Boolean = false
    private var imageUri : Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val materialToolbar: MaterialToolbar = binding.materialToolbar2
        materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        observeUI()
        setupObserver()
        if (imageUri != null){
            binding.productImageView.setImageURI(imageUri)
        }
        binding.productImageView.setOnClickListener {
            chooseImageDialog()
        }
        binding.saveBtn.setOnClickListener {
            addSellerProduct()
        }
        binding.previewBtn.setOnClickListener {
            if (binding.productNameEdt.text.toString() != "" && binding.ProductPriceEditText.text.toString() != "" && binding.descriptionEdt.text.toString() != "" && categoryID.size != 0 && binding.locationEdt.text.toString() != "") {
                previewProduct()
            } else {
                Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        logd("isgalery => $isImageFromGallery")
        if (!isImageFromGallery){
            logd("isg")
            cameraResult()
        }
    }

    private fun previewProduct() {
        val productName = binding.productNameEdt.text.toString()
        val productPrice = binding.ProductPriceEditText.text.toString().toInt()
        val productDescription = binding.descriptionEdt.text.toString()
        val category = categoryID.toString()
        val location = binding.locationEdt.text.toString()
        val previewProduct =
            PreviewProduct(
                productName = productName,
                productPrice = productPrice,
                productDescription = productDescription,
                category = category,
                categoryName = categoryName.joinToString {
                    it
                },
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
            val category = categoryID.toString().createPartFromString()
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
            .setPositiveButton("Gallery") { _, _ ->
                isImageFromGallery = true
                startGallery() }
            .setNegativeButton("Camera") { _, _ ->
                isImageFromGallery = false
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
            imageUri = selectedImg
            binding.productImageView.setImageURI(selectedImg)
        }
    }


    private fun setupObserver() {
        viewModel.category.observe(viewLifecycleOwner) { item ->
            when (item) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.categoryEditText.setOnClickListener {
                        showMultipleChoicesAlert(item.data)
                    }
                }
            }
        }
    }

    private fun observeUI() {
        viewModel.addSellerProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Gagal Menambah Product", Toast.LENGTH_LONG).show()
                    binding.previewBtn.isEnabled = true
                    binding.saveBtn.isEnabled = true
                    binding.saveBtn.text = "Simpan"
                }
                Result.Loading -> {
                    binding.previewBtn.isEnabled = false
                    binding.saveBtn.isEnabled = false
                    binding.saveBtn.text = "Loading ..."
                }
                is Result.Success -> {
                    navController.navigate(R.id.action_sellFragment_to_homeFragment)
                    Toast.makeText(requireContext(), "Berhasil Menambah Product", Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModelSeller.getAccount().observe(viewLifecycleOwner){profile ->
            when(profile) {
                is Result.Error -> {
                    Toast.makeText(requireContext(),"something went wrong", Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    if (profile.data.phoneNumber.isNullOrEmpty() || profile.data.city.isNullOrEmpty() || profile.data.address.isNullOrEmpty()){
                        profileIsIncomplete()
                    }
                }
            }
        }
    }
    private fun showMultipleChoicesAlert(categoryResponse: List<CategoryResponse>) {
        val selectedList = ArrayList<Int>()
        val selectedItems = ArrayList<String>()
        var items = arrayOf<String>()
        var id = arrayOf<Int>()
        for (i in categoryResponse) {
            items = items.plus(i.name)
            id = id.plus(i.id)
        }
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle("Pilih Kategori")
            .setMultiChoiceItems(items, null) { dialog, which, isChecked ->
                if (isChecked) {
                    selectedList.add(which)
                } else if (selectedList.contains(which)) {
                    selectedList.remove(which)
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                categoryName.removeAll(categoryName)
                categoryID.removeAll(categoryID)
                for (i in selectedList.indices) {
                    selectedItems.add(items[selectedList[i]])
                    categoryID.add(id[selectedList[i]])
                    categoryName.add(items[selectedList[i]])
                }
                binding.categoryEditText.setText(selectedItems.joinToString {
                    it
                })
                Log.d("id", "$categoryID")
            }
            .setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            .setNeutralButton("Bersihkan Pilihan") { ialog, which ->
                selectedItems.removeAll(selectedItems)
                categoryID.removeAll(categoryID)
                categoryName.removeAll(categoryName)
                binding.categoryEditText.setText(selectedItems.joinToString {
                    it
                })
            }
            .show()
    }
    private fun cameraResult(){
        val navController = navController
        val navBackStackEntry = navController.getBackStackEntry(R.id.sellFragment)
        if(navBackStackEntry.savedStateHandle.contains(CameraFragment.RESULT_KEY)){
            val result = navBackStackEntry.savedStateHandle.get<Bundle>(CameraFragment.RESULT_KEY)
            val myFile = result?.getSerializable("picture") as File
            isBackCamera = result.getBoolean("isBackCamera", true)

            getFile = myFile
            val resultFile = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            binding.productImageView.setImageBitmap(resultFile)
        }

    }
    private fun profileIsIncomplete(){
        binding.previewBtn.isEnabled = false
        binding.saveBtn.isEnabled = false
       AlertDialog.Builder(requireActivity())
           .setCancelable(false)
            .setMessage("Akun kamu belum lengkap nih!")
            .setPositiveButton("Lengkapi Akun") { _, _ ->
                findNavController().navigate(SellFragmentDirections.actionSellFragmentToEditAccountFragment())}
            .setNegativeButton("Abaikan") { dialog, _ ->
              dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }
}




