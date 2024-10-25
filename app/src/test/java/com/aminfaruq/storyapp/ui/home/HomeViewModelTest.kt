@file:Suppress("SameReturnValue")

package com.aminfaruq.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.aminfaruq.storyapp.DataDummy
import com.aminfaruq.storyapp.MainDispatcherRule
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.repository.StoryRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storiesRepository: StoryRepository

    private val differ = createDiffer()

    @Test
    fun `Get Story - If Return Success Should Not Null`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<StoryItemResponse> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItemResponse>>().apply { value = data }

        Mockito.`when`(storiesRepository.getStoryList()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storiesRepository)
        val actualStory = homeViewModel.story.asFlow().first()

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `Get Story returns first data successfully`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<StoryItemResponse> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItemResponse>>().apply { value = data }

        Mockito.`when`(storiesRepository.getStoryList()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storiesRepository)
        val actualStory = homeViewModel.story.asFlow().first()

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `Get Story - Should Return Zero If No Data Returned`() = runTest {
        val data: PagingData<StoryItemResponse> = StoryPagingSource.snapshot(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryItemResponse>>().apply { value = data }

        Mockito.`when`(storiesRepository.getStoryList()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storiesRepository)
        val actualStory = homeViewModel.story.asFlow().first()

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }

    private fun createDiffer(): AsyncPagingDataDiffer<StoryItemResponse> {
        return AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<StoryItemResponse>() {
                override fun areItemsTheSame(oldItem: StoryItemResponse, newItem: StoryItemResponse): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: StoryItemResponse, newItem: StoryItemResponse): Boolean {
                    return oldItem == newItem
                }
            },
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource: PagingSource<Int, LiveData<List<StoryItemResponse>>>() {
    companion object {
        fun snapshot(items: List<StoryItemResponse>): PagingData<StoryItemResponse> {
            return PagingData.from(items)
        }
    }

    @Suppress("SameReturnValue")
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItemResponse>>>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItemResponse>>> {
        return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
    }

}