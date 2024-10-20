package com.aminfaruq.storyapp.di

import android.content.Context
import com.aminfaruq.storyapp.data.api.ApiConfig
import com.aminfaruq.storyapp.data.api.ApiService
import com.aminfaruq.storyapp.repository.AuthRepository
import com.aminfaruq.storyapp.repository.StoryRepository
import com.aminfaruq.storyapp.utils.viewmodelfactory.AuthViewModelFactory
import com.aminfaruq.storyapp.utils.viewmodelfactory.StoryViewModelFactory

object Injection {
    // Fungsi untuk menyediakan instance ApiService
    private fun provideApiService(context: Context): ApiService {
        return ApiConfig.getApiService(context)
    }

    // Fungsi untuk menyediakan instance AuthRepository
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = provideApiService(context)
        return AuthRepository.getInstance(apiService)
    }

    // Fungsi untuk menyediakan instance StoryRepository
    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = provideApiService(context)
        return StoryRepository.getInstance(apiService)
    }

    // Fungsi untuk menyediakan AuthViewModelFactory
    fun provideAuthViewModelFactory(context: Context): AuthViewModelFactory {
        val authRepository = provideAuthRepository(context)
        return AuthViewModelFactory(authRepository)
    }

    // Fungsi untuk menyediakan StoryViewModelFactory
    fun provideStoryViewModelFactory(context: Context): StoryViewModelFactory {
        val storyRepository = provideStoryRepository(context)
        return StoryViewModelFactory(storyRepository)
    }
}