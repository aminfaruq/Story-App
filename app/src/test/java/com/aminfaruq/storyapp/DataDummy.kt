package com.aminfaruq.storyapp

import com.aminfaruq.storyapp.data.response.story.StoryItemResponse

object DataDummy {
    fun generateDummyQuoteResponse(): List<StoryItemResponse> {
        val items: MutableList<StoryItemResponse> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItemResponse(
                id = i.toString(),
                photoUrl = "name + $i",
                createdAt = "description $i",
                name = "photoUrl https://picsum.photos/$i",
                description = "createdAt $i",
                lon = 40.7434,
                lat = 74.0080,
            )
            items.add(story)
        }
        return items
    }
}