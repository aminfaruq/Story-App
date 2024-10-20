package com.aminfaruq.storyapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aminfaruq.storyapp.data.api.ApiService
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import com.aminfaruq.storyapp.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService
) {
    // Fungsi untuk mendapatkan daftar cerita
    fun getStoryList(size: Int): LiveData<Result<StoryListResponse>> = liveData {
        emit(Result.Loading) // Emit loading state
        try {
            // Panggil API untuk mendapatkan daftar cerita
            val response = apiService.getStoryList(size)

            if (response.error) {
                // Emit error jika API mengembalikan error
                emit(Result.Error(response.message))
            } else {
                // Emit success jika data berhasil diambil
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            // Emit error jika ada exception
            emit(Result.Error(e.localizedMessage ?: "Error fetching stories"))
        }
    }

    // Fungsi untuk mengunggah gambar
    fun uploadImage(
        context: Context,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<MessageResponse>> = liveData {
        emit(Result.Loading) // Emit loading state
        try {
            // Panggil API untuk mengunggah gambar
            val response = apiService.doUploadImage(file, description)

            if (response.error) {
                // Emit error jika API mengembalikan error
                emit(Result.Error(response.message))
            } else {
                // Emit success jika upload berhasil
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            // Emit error jika ada exception
            emit(Result.Error(e.localizedMessage ?: "Error uploading image"))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService).also { instance = it }
            }
    }
}
