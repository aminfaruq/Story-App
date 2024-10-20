package com.aminfaruq.storyapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aminfaruq.storyapp.data.api.ApiService
import com.aminfaruq.storyapp.data.response.login.AuthResponse
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.utils.Result
import com.aminfaruq.storyapp.utils.SharedPreferencesHelper

class AuthRepository private constructor(
    private val apiService: ApiService,
) {

    // Fungsi untuk login
    fun login(email: String, password: String, context: Context): LiveData<Result<AuthResponse>> = liveData {
        val sharedPreferencesHelper = SharedPreferencesHelper(context)

        emit(Result.Loading) // Emit loading state
        try {
            // Panggil API untuk login
            val response = apiService.doLogin(email, password)

            if (response.error) {
                // Emit error jika respons API menunjukkan error
                emit(Result.Error(response.message))
            } else {
                // Simpan token ke SharedPreferences setelah login berhasil
                sharedPreferencesHelper.saveToken(response.loginResult.token)
                sharedPreferencesHelper.saveName(response.loginResult.name)
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            // Emit error jika terjadi exception
            emit(Result.Error(e.localizedMessage ?: "Error occurred during login"))
        }
    }

    // Fungsi untuk register
    fun register(name: String, email: String, password: String): LiveData<Result<MessageResponse>> = liveData {
        emit(Result.Loading) // Emit loading state
        try {
            // Panggil API untuk register
            val response = apiService.doRegister(name, email, password)

            if (response.error) {
                // Emit error jika respons API menunjukkan error
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            // Emit error jika terjadi exception
            emit(Result.Error(e.localizedMessage ?: "Error occurred during registration"))
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiService): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService).also { instance = it }
            }
    }
}