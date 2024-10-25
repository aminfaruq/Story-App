package com.aminfaruq.storyapp.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aminfaruq.storyapp.repository.StoryRepository
import com.aminfaruq.storyapp.ui.detail.DetailViewModel
import com.aminfaruq.storyapp.ui.home.HomeViewModel
import com.aminfaruq.storyapp.ui.maps.MapsViewModel
import com.aminfaruq.storyapp.ui.upload.UploadViewModel

class StoryViewModelFactory(
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(storyRepository) as T
        }

        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UploadViewModel(storyRepository) as T
        }

        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(storyRepository) as T
        }

        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
