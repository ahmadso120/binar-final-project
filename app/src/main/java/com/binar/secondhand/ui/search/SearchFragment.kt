package com.binar.secondhand.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSearchBinding
import com.binar.secondhand.ui.home.HomeFragmentDirections
import com.binar.secondhand.utils.EventObserver
import com.binar.secondhand.utils.ui.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    override var bottomNavigationViewVisibility = View.GONE

    private val viewModel by viewModel<SearchViewModel>()

    private val binding: FragmentSearchBinding by viewBinding()

    private lateinit var productAdapter: SearchAdapterProduct

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search1Et.focusAndShowKeyboard()

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        binding.cancelSearch.setOnClickListener{
            binding.search1Et.setText("")
            cancel()
        }
        setupAdapter()
        doSomething(binding.search1Et)
    }

    private fun setupAdapter() {

        val itemSpacing = resources.getDimensionPixelSize(R.dimen.margin_padding_size_medium)

        productAdapter = SearchAdapterProduct {
            viewModel.onBuyerProductClicked(it)
        }

        binding.searchList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setupLayoutManager(
                spacing = itemSpacing
            )
            setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
        }
    }

    private fun getQuery(){
        val search =binding.search1Et.text.toString()
        if(search.isNotEmpty()){
            viewModel.getData(search)
        }else{
            view?.showShortSnackbar("isi pencarian")
        }

    }

    private fun showData(){

            view?.hideKeyboard()
            viewModel.search.observe(viewLifecycleOwner){
                when(it) {
                    is Result.Error -> {
                        it.error?.let { err ->
                            view?.showShortSnackbar(err)
                        }
                    }
                    Result.Loading -> {
                        loadingUi()
                    }
                    is Result.Success -> {
                        val data = it.data
                        if (data.isNotEmpty()){
                            productAdapter.submitList(data)
                            successUi()
                            viewModel.navigateToBuyerProductDetail.observe(viewLifecycleOwner, EventObserver {
                                val action = SearchFragmentDirections.actionSearchFragmentToProductDetailFragment(it.id)
                                findNavController().navigate(action)
                            })

                        }else{
                            notFoundUi()
                        }

                    }
                }
            }
    }


    private fun doSomething(search: EditText){

        search.setOnEditorActionListener(TextView.OnEditorActionListener{ _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getQuery()
                showData()
                return@OnEditorActionListener true

            }

            false

        })

    }

    private fun successUi(){
        binding.searchHistory.visibility = View.GONE
        binding.searchList.visibility = View.VISIBLE

        binding.notfoundTv.visibility = View.GONE
        binding.contentLoadingLayout.visibility = View.GONE
    }
    private fun loadingUi(){
        binding.searchHistory.visibility = View.GONE
        binding.searchList.visibility = View.GONE

        binding.notfoundTv.visibility = View.GONE
        binding.contentLoadingLayout.visibility = View.VISIBLE
    }
    private fun notFoundUi(){
        binding.searchHistory.visibility = View.GONE
        binding.searchList.visibility = View.GONE

        binding.notfoundTv.visibility = View.VISIBLE
        binding.contentLoadingLayout.visibility = View.GONE

    }
    private fun cancel(){
        binding.searchHistory.visibility = View.VISIBLE
        binding.searchList.visibility = View.GONE

        binding.notfoundTv.visibility = View.GONE
        binding.contentLoadingLayout.visibility = View.GONE
        binding.search1Et.focusAndShowKeyboard()
    }
}

