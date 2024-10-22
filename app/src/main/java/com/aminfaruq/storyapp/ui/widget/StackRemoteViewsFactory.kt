package com.aminfaruq.storyapp.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.di.Injection
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private val storiesBitmap = arrayListOf<Bitmap>()
    private val stories = arrayListOf<StoryItemResponse>()

    override fun onCreate() {}

    override fun onDataSetChanged() = runBlocking {
        try {
            val repository = Injection.provideStoryRepository(context)
            val storyList = withContext(Dispatchers.IO) {
                repository.getStoryListFromApi()
            }

            stories.clear()
            storiesBitmap.clear()

            stories.addAll(storyList.listStory)
            storiesBitmap.addAll(storyList.listStory.map {
                Glide.with(context)
                    .asBitmap()
                    .load(it.photoUrl)
                    .override(256, 256)
                    .submit()
                    .get()
            })

            StoryAppWidget.notifyDataSetChanged(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onDestroy() {}

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_story_item)

        if (stories.isNotEmpty()) {
            val bitmap = storiesBitmap[position]
            rv.setImageViewBitmap(R.id.imageViewWidget, bitmap)
        }

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}
