package com.binar.secondhand.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.databinding.FragmentSearchBinding
import com.binar.secondhand.ui.common.ProductAdapter
import com.binar.secondhand.ui.home.HomeViewModel
import com.binar.secondhand.utils.ui.RECYCLER_VIEW_CACHE_SIZE
import com.binar.secondhand.utils.ui.setupLayoutManager
import com.binar.secondhand.utils.ui.showShortSnackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment(R.layout.fragment_search) {
    override var bottomNavigationViewVisibility = View.GONE
    private val viewModel by viewModel<SearchViewModel>()
    private val binding: FragmentSearchBinding by viewBinding()
    private lateinit var productAdapter: SearchAdapterProduct
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        showData()
    }

    private fun setupAdapter() {
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.margin_padding_size_medium)

        productAdapter = SearchAdapterProduct {
            viewModel.onBuyerProductClicked(it)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setupLayoutManager(
                spacing = itemSpacing
            )
            setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
        }
    }

    private fun getQuery(){

        val search =binding.search1.text.toString()
        Log.d("search",binding.search1.text.toString())
        if(search.isNotEmpty()){
            viewModel.getData(search)
        }else{
            view?.showShortSnackbar("isi pencarian")
        }

    }

    private fun showData(){
        binding.imageButton2.setOnClickListener {
            getQuery()
            viewModel.search.observe(viewLifecycleOwner){
                when(it) {
                    is Result.Error -> {
                        it.error?.let { err ->
                            view?.showShortSnackbar(err)
                        }
                    }
                    Result.Loading -> {
                    }
                    is Result.Success -> {
                        productAdapter.submitList(it.data)
                        Log.d("adapt",it.data.toString())
                    }
                }
            }
        }
    }


}