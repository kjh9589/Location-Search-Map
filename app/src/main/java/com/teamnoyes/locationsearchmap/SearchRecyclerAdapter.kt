package com.teamnoyes.locationsearchmap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamnoyes.locationsearchmap.databinding.ItemSearchResultBinding
import com.teamnoyes.locationsearchmap.model.SearchResultEntity

class SearchRecyclerAdapter(private val onItemClicked: (SearchResultEntity) -> Unit) :
    ListAdapter<SearchResultEntity, SearchRecyclerAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchResultEntity>(){
            override fun areItemsTheSame(
                oldItem: SearchResultEntity,
                newItem: SearchResultEntity
            ): Boolean {
                return oldItem.fullAddress == newItem.fullAddress
            }

            override fun areContentsTheSame(
                oldItem: SearchResultEntity,
                newItem: SearchResultEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResultEntity: SearchResultEntity) {
            binding.titleTextView.text = searchResultEntity.buildingName
            binding.subtitleTextView.text = searchResultEntity.fullAddress

            binding.root.setOnClickListener {
                onItemClicked(searchResultEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}