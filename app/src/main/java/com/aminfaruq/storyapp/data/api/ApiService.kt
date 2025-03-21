package com.aminfaruq.storyapp.data.api

import com.aminfaruq.storyapp.data.response.login.AuthResponse
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.data.response.story.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): MessageResponse

    @GET("stories")
    suspend fun getStoryList(
        @Query("size") size:Int
    ): StoryListResponse

    @Multipart
    @POST("stories")
    suspend fun doUploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): MessageResponse
}