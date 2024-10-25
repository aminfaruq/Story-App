package com.aminfaruq.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import com.aminfaruq.storyapp.repository.StoryRepository
import com.aminfaruq.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getLocation(): LiveData<Result<StoryListResponse>> = storyRepository.getStoryLocation()
}