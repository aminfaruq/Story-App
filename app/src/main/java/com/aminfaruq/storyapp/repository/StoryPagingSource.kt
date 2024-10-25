package com.aminfaruq.storyapp.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aminfaruq.storyapp.data.api.ApiService
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryItemResponse>()  {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItemResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoryList(page = position, size = params.loadSize)

            Log.d("StoryPagingSource", "Loaded page $position with ${responseData.listStory.size} items")

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Error loading data", exception)
            return LoadResult.Error(exception)
        }
    }

}