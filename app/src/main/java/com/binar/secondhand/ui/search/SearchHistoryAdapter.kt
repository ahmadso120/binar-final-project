package com.binar.secondhand.ui.search


import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.databinding.FragmentSearchBinding
import com.binar.secondhand.databinding.HistoryItemBinding


class SearchHistoryAdapter(val listResult : List<SearchHistory>)
:RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SearchHistory>() {
        override fun areItemsTheSame(
            oldItem: SearchHistory,
            newItem: SearchHistory
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SearchHistory,
            newItem: SearchHistory
        ): Boolean = oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<SearchHistory>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val search = FragmentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = differ.currentList[position]
//        data.let { holder.bind(data) }
        holder.bind(listResult[position])
    }

    override fun getItemCount(): Int = listResult.size


//    interface OnClickListener {
//        fun onClickItem(data: SearchHistory)
//    }


    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data1: SearchHistory) {
            binding.apply {

                historyTv.text = data1.historySearch

                root.setOnClickListener {
                    it.findNavController().navigate(SearchFragmentDirections.actionGlobalToHomeFragment())
                }


            }
        }
    }
}