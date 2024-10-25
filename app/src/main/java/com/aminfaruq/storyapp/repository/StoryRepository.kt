package com.aminfaruq.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.aminfaruq.storyapp.data.api.ApiService
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import com.aminfaruq.storyapp.utils.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService
) {
    fun getStoryLocation(
        location: Int? = 1
    ): LiveData<Result<StoryListResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryList(location)

            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Error fetching stories"))
        }
    }

    fun getStoryList(): LiveData<PagingData<StoryItemResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getStoryListFromApi(location: Int? = 0): StoryListResponse {
        return try {
            val response = apiService.getStoryList(location)
            if (response.error) {
                throw Exception(response.message)
            } else {
                response
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            throw Exception(errorResponse.message)
        } catch (e: Exception) {
            throw Exception(e.localizedMessage ?: "Error fetching stories")
        }
    }

    fun uploadImage(
        file: File,
        description: String
    ): LiveData<Result<MessageResponse>> = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        try {
            val response = apiService.doUploadImage(multipartBody, requestBody)

            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Error uploading image"))
        }
    }

    fun getStoryDetail(id: String): LiveData<Result<StoryListResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryDetail(id)

            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Error fetching story detail"))
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
