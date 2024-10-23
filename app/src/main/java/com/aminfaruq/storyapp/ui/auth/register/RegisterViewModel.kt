package com.aminfaruq.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.repository.AuthRepository
import com.aminfaruq.storyapp.utils.Result

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<Result<MessageResponse>> {
        return authRepository.register(name, email, password)
    }
}
