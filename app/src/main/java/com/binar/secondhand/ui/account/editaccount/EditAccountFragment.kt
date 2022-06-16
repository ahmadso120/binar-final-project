package com.binar.secondhand.ui.account.editaccount


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentEditAccountBinding
import com.binar.secondhand.ui.camera.CameraFragment.Companion.RESULT_KEY
import com.binar.secondhand.utils.loadPhotoUrl
import com.binar.secondhand.utils.rotateBitmap
import com.binar.secondhand.utils.showShortSnackbar
import com.binar.secondhand.utils.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class EditAccountFragment : BaseFragment(R.layout.fragment_edit_account) {
    private var getFile: File? = null
    private var isImageFromGallery: Boolean = false
    private var isBackCamera: Boolean = false
    private val binding: FragmentEditAccountBinding by viewBinding()

    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<EditAccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImageView.setOnClickListener {
         chooseImageDialog()

        }
        observeUI()
        setupObserver()


    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("choose Image")
            .setPositiveButton("Gallery") { _, _ -> startGallery() }
            .setNegativeButton("Camera") { _, _ ->
                findNavController().navigate(R.id.action_editAccountFragment_to_cameraFragment)
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

    private fun setupObserver() {
        val navController = findNavController()
        val navBackStackEntry = navController.getBackStackEntry(R.id.editAccountFragment)

        val observerResultKey = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(RESULT_KEY)
                ) {
                val result = navBackStackEntry.savedStateHandle.get<Bundle>(RESULT_KEY)
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
                        phoneNumberEdt.setText(it.data.phoneNumber)
                        if (it.data.imageUrl != null || getFile == null) {
                            with(profileImageView) { it.data.imageUrl?.let { it1 -> loadPhotoUrl(it1) } }
                        }
                    }

                }
            }
        }
    }
}