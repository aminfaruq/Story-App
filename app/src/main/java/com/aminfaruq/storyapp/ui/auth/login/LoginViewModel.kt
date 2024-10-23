package com.aminfaruq.storyapp.ui.auth.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aminfaruq.storyapp.data.response.login.AuthResponse
import com.aminfaruq.storyapp.repository.AuthRepository
import com.aminfaruq.storyapp.utils.Result

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String, context: Context): LiveData<Result<AuthResponse>> {
        return authRepository.login(email, password, context)
    }
}
