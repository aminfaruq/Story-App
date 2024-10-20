package com.aminfaruq.storyapp.data.response.story

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)