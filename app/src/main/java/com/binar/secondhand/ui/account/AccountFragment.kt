package com.binar.secondhand.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.databinding.FragmentAccountBinding
import com.binar.secondhand.databinding.FragmentHomeBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.utils.LogoutProcess
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private val binding: FragmentAccountBinding by viewBinding()
    override var bottomNavigationViewVisibility = View.GONE


    private val viewModel by viewModel<AccountViewModel>()
    private val appLocalData: AppLocalData by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUI()



        binding.imgCam.setOnClickListener {
            val product = getItem()


            if (!response.data.imageUrl.isNullOrEmpty) {
                binding.imgCam.loadPhotoUrl(response.data.imageUrl)
            } else {
            // ambil dari drawable
                R.drawable.imguser
            }





        }
        binding.icEdit.setOnClickListener {
            /*findNavController().navigate(R.id.)
*/
        }
        binding.icSettings.setOnClickListener {

        }
/*        binding.icLogout.setOnClickListener {
            LogoutProcess.execute(appLocalData, binding)        }*/
    }

    private fun observeUI() {
        binding.icLogout.setOnClickListener {
            LogoutProcess.execute(appLocalData, binding)
        }



    }

}


}