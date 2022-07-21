package com.binar.secondhand.ui.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binar.secondhand.data.source.local.entity.SearchHistory
import com.binar.secondhand.databinding.HistoryItemBinding




class SearchHistoryAdapter (
    private var onDetailClick: (SearchHistory) -> Unit
) : ListAdapter<SearchHistory,SearchHistoryAdapter.ProductViewHolder>(ProductDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {

            historyTv.text = data.historySearch

            root.setOnClickListener {
                onDetailClick(data)
            }

        }
    }

    class ProductViewHolder(
        val binding: HistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ProductDiffCallBack: DiffUtil.ItemCallback<SearchHistory>() {
        override fun areItemsTheSame(
            oldItem: SearchHistory,
            newItem: SearchHistory
        ): Boolean {
            return oldItem.historySearch == newItem.historySearch
        }

        override fun areContentsTheSame(
            oldItem: SearchHistory,
            newItem: SearchHistory
        ): Boolean {
            return oldItem == newItem
        }
    }
}