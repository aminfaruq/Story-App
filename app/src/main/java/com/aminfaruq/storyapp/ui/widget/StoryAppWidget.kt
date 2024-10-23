package com.aminfaruq.storyapp.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.ui.detail.DetailActivity


/**
 * Implementation of App Widget functionality.
 */
class StoryAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StackWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

        val views = RemoteViews(context.packageName, R.layout.story_widget)
        views.setRemoteAdapter(R.id.stackView, intent)
        views.setEmptyView(R.id.stackView, R.id.empty_view)

        val detailIntent = Intent(context, DetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setPendingIntentTemplate(R.id.stackView, pendingIntent)

        notifyDataSetChanged(context)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


    companion object {
        fun notifyDataSetChanged(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, StoryAppWidget::class.java)
            manager.notifyAppWidgetViewDataChanged(
                manager.getAppWidgetIds(componentName),
                R.id.stackView
            )
        }
    }
}
