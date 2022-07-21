package com.binar.secondhand.ui.account.history

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentHistoryBinding
import com.binar.secondhand.utils.logd
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryFragment : BaseFragment(R.layout.fragment_history) {
    private val binding: FragmentHistoryBinding by viewBinding()
    private val viewModel by viewModel<HistoryViewModel>()
    override var bottomNavigationViewVisibility = View.GONE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.materialToolbar2
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        observeUI()
    }
    private fun observeUI() {

        viewModel.getAllHistory().observe(viewLifecycleOwner) {
            when(it){
                is Result.Error -> {

                }
                Result.Loading -> {}
                is Result.Success -> {
                    val layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    binding.recyclerview.layoutManager = layoutManager
                    binding.recyclerview.adapter = HistoryAdapter(it.data)
                }
            }

        }
    }
}