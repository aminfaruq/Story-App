package com.aminfaruq.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import com.aminfaruq.storyapp.repository.StoryRepository
import com.aminfaruq.storyapp.utils.Result

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _listStory = MutableLiveData<StoryListResponse>()
    val listStory: LiveData<StoryListResponse> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun requestStoryList(
        location: Int? = 0
    ) {
        storyRepository.getStoryList(location).observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listStory.value = result.data
                    _isError.value = false
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _isError.value = true
                }
            }
        }
    }

}
