package com.binar.secondhand.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.binar.secondhand.R
import com.binar.secondhand.base.BaseFragment
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.databinding.FragmentSearchBinding
import com.binar.secondhand.ui.home.HomeFragmentDirections
import com.binar.secondhand.utils.EventObserver
import com.binar.secondhand.utils.ui.*
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment(R.layout.fragment_search) {
    override var bottomNavigationViewVisibility = View.GONE
    private val viewModel by viewModel<SearchViewModel>()
    private val binding: FragmentSearchBinding by viewBinding()
    private lateinit var productAdapter: SearchAdapterProduct
    private lateinit var historyAdapter: SearchHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search1Et.focusAndShowKeyboard()
        val materialToolbar: MaterialToolbar = binding.materialToolbar2
        materialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.cancelSearch.setOnClickListener{
            binding.search1Et.setText("")
            cancel()
        }
//        adapterHistory()
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
            val searchEntity = SearchHistory(
                id=null,
                historySearch = search
            )
//            viewModel.insertHistory(searchEntity)

//            viewModel.historyString.observe(viewLifecycleOwner){
//                if (search != it.historySearch){
//                    viewModel.insertHistory(searchEntity)
//                    Log.d("test",it.historySearch.toString())
//                }
//            }

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
    private fun adapterHistory(){
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.searchHistory.layoutManager = layoutManager

            viewModel.history.observe(viewLifecycleOwner){
                historyAdapter = SearchHistoryAdapter(it)
                historyAdapter.submitData(it)
                binding.searchHistory.adapter = historyAdapter
                binding.searchHistory.visibility = View.VISIBLE
                binding.searchHistory.setOnClickListener {
                    binding.search1Et.setText("halo")
                }
            }


    }

    private fun historyClick(){
        binding.apply {

        }
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

