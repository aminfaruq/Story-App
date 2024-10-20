package com.aminfaruq.storyapp.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aminfaruq.storyapp.repository.StoryRepository

class StoryViewModelFactory(
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //TODO: Implement factory
//        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return StoryViewModel(storyRepository) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
