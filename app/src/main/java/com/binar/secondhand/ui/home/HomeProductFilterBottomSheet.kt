package com.binar.secondhand.ui.home

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.CategoryResponse
import com.binar.secondhand.databinding.BottomSheetHomeProductFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeProductFilterBottomSheet : BottomSheetDialogFragment() {

    private val binding: BottomSheetHomeProductFilterBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: HomeViewModel by sharedViewModel()

    private var homeParametersChanged = false

    private var catId = 0

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

        viewModel.categories.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    val items = it.data.plus(CategoryResponse(0, "Pilih Kategori"))
                    val names = items.map { names -> names.name }
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_list, names)
                    val categoryFilterDropdownMenu = binding.categoryFilterDropdownMenu.editText as? AutoCompleteTextView
                    categoryFilterDropdownMenu?.setAdapter(adapter)

                    val name = items.filter { categoryFilter ->
                        categoryFilter.id == viewModel.categoryId
                    }

                    categoryFilterDropdownMenu?.setText(
                        name[0].name,
                        false
                    )

                    categoryFilterDropdownMenu?.setOnItemClickListener { _, _, position, _ ->
                        val categoryId = items[position].id
                        if (categoryId != viewModel.categoryId) {
                            homeParametersChanged = true
                            catId = categoryId
                            viewModel.categoryId = categoryId
                        }
                    }
                }
            }
        }

        binding.applyButton.setOnClickListener { dismiss() }
        if (viewModel.categoryId > 0) {
            binding.resetTv.isVisible = true
            binding.resetTv.setOnClickListener {
                viewModel.filterCategoryProduct(0)
                catId = 0
                viewModel.categoryId = 0
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (homeParametersChanged) {
            viewModel.filterCategoryProduct(catId)
        }
    }

    companion object {

        val TAG: String = HomeProductFilterBottomSheet::class.java.simpleName

        fun newInstance() = HomeProductFilterBottomSheet()
    }
}