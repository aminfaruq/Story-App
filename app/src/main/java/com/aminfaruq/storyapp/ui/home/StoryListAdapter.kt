package com.aminfaruq.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.databinding.StoryListBinding
import com.bumptech.glide.Glide

interface OnItemClickListener {
    fun onItemClick(id: String)
}

class StoryListAdapter(
    private val listStory: List<StoryItemResponse>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {

    class StoryViewHolder(val binding: StoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = listStory[position]
        holder.binding.apply {
            title.text = item.name
            description.text = item.description
            Glide.with(holder.itemView.context)
                .load(item.photoUrl)
                .into(imageView)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(item.id)
        }
    }
}
