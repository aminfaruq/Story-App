package com.aminfaruq.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import com.aminfaruq.storyapp.repository.StoryRepository
import com.aminfaruq.storyapp.utils.Result

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getDetailId(id: String): LiveData<Result<StoryListResponse>> =
        storyRepository.getStoryDetail(id)
}
