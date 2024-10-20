package com.aminfaruq.storyapp.data.response.login

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @field:SerializedName("loginResult")
    val loginResult: UserResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)