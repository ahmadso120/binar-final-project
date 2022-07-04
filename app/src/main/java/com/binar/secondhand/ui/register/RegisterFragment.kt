package com.binar.secondhand.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.databinding.FragmentRegisterBinding
import com.binar.secondhand.ui.camera.CameraFragment
import com.binar.secondhand.utils.*
import com.binar.secondhand.utils.ui.showShortSnackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val binding: FragmentRegisterBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE
    private val viewModel by viewModel<RegisterViewModel>()

    private var getFile: File? = null
    private var isImageFromGallery: Boolean = false
    private var isBackCamera: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerBtn.setOnClickListener {
                register()
            }
            profileImageView.setOnClickListener{
                chooseImageDialog()
            }

        }
        observeUI()
        setupObserve()
    }
    private fun register(){
        val fullName = binding.nameEdt.text.toString().createPartFromString()
        val email = binding.emailEdt.text.toString().createPartFromString()
        val password = binding.passwordEdt.text.toString().createPartFromString()
        val phoneNumber = binding.phoneNumberEdt.text.toString().createPartFromString()
        val address = binding.addressEdt.text.toString().createPartFromString()
        val city = binding.cityEdt.text.toString().createPartFromString()
        val map = HashMap<String, RequestBody>().apply {
            put("full_name", fullName)
            put("email",email)
            put("password", password)
            put("phone_number", phoneNumber)
            put("address", address)
            put("city",city)
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


            val accountRequest = RegisterRequest(
                imageMultipart,
                map
            )
            logd("acc $accountRequest")
            viewModel.doRegisterRequest(accountRequest)
        }else{
            val accountRequest = RegisterRequest(
                file = null,
                map
            )
            logd("acc $accountRequest")
            viewModel.doRegisterRequest(accountRequest)

        }
    }
    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("choose Image")
            .setPositiveButton("Gallery") { _, _ -> startGallery() }
            .setNegativeButton("Camera") { _, _ ->
                findNavController().navigate(R.id.action_registerFragment_to_cameraFragment)
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
            binding.profileImageView.setImageURI(selectedImg)
        }
    }



    private fun setupObserve(){
        val navController = findNavController()
        val navBackStackEntry = navController.getBackStackEntry(R.id.registerFragment)
        val window = activity?.window
        window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white)
        val observerResultKey = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(CameraFragment.RESULT_KEY)
            ) {
                val result = navBackStackEntry.savedStateHandle.get<Bundle>(CameraFragment.RESULT_KEY)
                val myFile = result?.getSerializable("picture") as File
                isBackCamera = result.getBoolean("isBackCamera", true)

                getFile = myFile
                isImageFromGallery = false

                val resultFile = rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
                binding.profileImageView.setImageBitmap(resultFile)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observerResultKey)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observerResultKey)
            }
        })
    }

    private fun observeUI(){
        viewModel.register.observe(viewLifecycleOwner){
            when(it){
                is Result.Error ->{

                    it.error?.let { error ->
                        view?.showShortSnackbar(error)
                    }
                }
            Result.Loading->{
                binding.registerBtn.text = "loading"
            }
                is Result.Success ->{
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }


}