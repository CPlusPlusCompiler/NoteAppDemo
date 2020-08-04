package com.andrius.notesappdemo.fragments

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.andrius.notesappdemo.R

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [SingleNoteWidgetConfigureActivity]
 */
class SingleNoteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray)
    {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds)
        {
            SingleNoteWidgetConfigureActivity.deleteTitlePref(
                context,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object
    {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int)
        {

            val widgetText =
                SingleNoteWidgetConfigureActivity.loadTitlePref(
                    context,
                    appWidgetId
                )
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName,
                R.layout.single_note_widget
            )
            views.setTextViewText(R.id.widget_text, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
