package com.aminfaruq.storyapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.databinding.StoryListBinding
import com.bumptech.glide.Glide

interface OnItemClickListener {
    fun onItemClick(id: String)
}

class StoryListAdapter(

    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<StoryItemResponse, StoryListAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(private val binding: StoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryItemResponse) {
            binding.apply {
                title.text = item.name
                description.text = item.description
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

        holder.itemView.setOnClickListener {
            data?.id?.let { onItemClickListener.onItemClick(it) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItemResponse>() {
            override fun areItemsTheSame(
                oldItem: StoryItemResponse,
                newItem: StoryItemResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryItemResponse,
                newItem: StoryItemResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
