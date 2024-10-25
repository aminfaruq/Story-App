package com.aminfaruq.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.repository.StoryRepository

class HomeViewModel(storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<StoryItemResponse>> =
        storyRepository.getStoryList().cachedIn(viewModelScope)
}
