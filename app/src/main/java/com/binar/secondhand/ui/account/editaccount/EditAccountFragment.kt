package com.binar.secondhand.ui.account.editaccount

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountRequest
import com.binar.secondhand.databinding.FragmentEditAccountBinding
import com.binar.secondhand.ui.camera.CameraFragment.Companion.RESULT_KEY
import com.binar.secondhand.utils.*
import com.binar.secondhand.utils.ui.loadPhotoUrl
import com.binar.secondhand.utils.ui.showShortSnackbar
import com.google.android.material.appbar.MaterialToolbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditAccountFragment : BaseFragment(R.layout.fragment_edit_account) {
    private var getFile: File? = null
    private var isImageFromGallery: Boolean = true
    private var isBackCamera: Boolean = false
    private val binding: FragmentEditAccountBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE
    private val viewModel by viewModel<EditAccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val materialToolbar: MaterialToolbar = binding.materialToolbar2
        materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.initialsTextView.setOnClickListener {
            chooseImageDialog()
        }

        binding.profileImageView.setOnClickListener {
            chooseImageDialog()

        }
        binding.saveBtn.setOnClickListener {
            updateAccount()
        }

        observeUI()
    }

    override fun onResume() {
        super.onResume()
        logd("isgalery => $isImageFromGallery")
        if (!isImageFromGallery){
            logd("isg")
            cameraResult()
        }
    }

    private fun updateAccount() {
        val fullName = binding.nameEdt.text.toString().createPartFromString()
        val address = binding.addressEdt.text.toString().createPartFromString()
        val phoneNumber = binding.phoneNumberEdt.text.toString().createPartFromString()
        val city = binding.cityEdt.text.toString().createPartFromString()


        val map = HashMap<String, RequestBody>().apply {
            put("full_name", fullName)
            put("address", address)
            put("phone_number", phoneNumber)
            put("city", city)
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
            val accountRequest = AccountRequest(
                imageMultipart,
                map
            )
            logd("acc $accountRequest")
            viewModel.doUpdateAccountRequest(accountRequest)
        }else{
            val accountRequest = AccountRequest(
                file = null,
                map
            )
            logd("acc $accountRequest")
            viewModel.doUpdateAccountRequest(accountRequest)

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
                navController.navigate(R.id.action_editAccountFragment_to_cameraFragment)
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
            binding.profileImageCardView.visibility = View.VISIBLE
            binding.initialsTextView.visibility = View.GONE
            binding.profileImageView.setImageURI(selectedImg)
        }
    }


    private fun observeUI() {
        viewModel.getAccount().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    binding.apply {
                        nameEdt.setText(it.data.fullName)
                        addressEdt.setText(it.data.address)
                        cityEdt.setText(it.data.city)
                        phoneNumberEdt.setText(it.data.phoneNumber)
                        if (getFile == null) {
                            if (it.data.imageUrl.isNullOrEmpty()){
                                initialsTextView.visibility = View.VISIBLE
                                val name = it.data.fullName
                                val initials = name.trim()
                                    .splitToSequence(" ", limit = 2)
                                    .map { it.first() }
                                    .joinToString("").uppercase()
                               initialsTextView.text = initials
                            }else{
                                profileImageCardView.visibility = View.VISIBLE
                                with(profileImageView) { it.data.imageUrl.let { url -> loadPhotoUrl(url) } }
                            }

                        }
                    }

                }
            }
        }

        viewModel.updateAccount.observe(viewLifecycleOwner){
        when(it){
            is Result.Error -> {
                Toast.makeText(requireContext(),"Update Failed",Toast.LENGTH_SHORT).show()
                binding.apply {
                    saveBtn.text = "Simpan"
                    saveBtn.isEnabled = true
                }

            }
            Result.Loading -> {
                binding.apply {
                    saveBtn.text = "Loading...."
                    saveBtn.isEnabled = false
                }

            }
            is Result.Success ->{
                Toast.makeText(requireContext(),"Update Success",Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_editAccountFragment_to_homeFragment)
            }
        }
        }
    }

    private fun cameraResult(){
        val navController = navController
        val navBackStackEntry = navController.getBackStackEntry(R.id.editAccountFragment)
        if(navBackStackEntry.savedStateHandle.contains(RESULT_KEY)){
            val result = navBackStackEntry.savedStateHandle.get<Bundle>(RESULT_KEY)
            val myFile = result?.getSerializable("picture") as File
            isBackCamera = result.getBoolean("isBackCamera", true)

            getFile = myFile
            val resultFile = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            binding.profileImageCardView.visibility = View.VISIBLE
            binding.initialsTextView.visibility = View.GONE
            binding.profileImageView.setImageBitmap(resultFile)
        }

    }
}