package com.aminfaruq.storyapp.ui.upload

import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.repository.StoryRepository
import java.io.File

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) =
        storyRepository.uploadImage(file, description)
}
