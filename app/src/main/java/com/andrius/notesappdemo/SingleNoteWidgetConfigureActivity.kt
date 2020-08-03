package com.andrius.notesappdemo

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.andrius.notesappdemo.adapters.NotesAdapter
import com.andrius.notesappdemo.models.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.single_note_widget_configure.*

/**
 * The configuration screen for the [SingleNoteWidget] AppWidget.
 */
class SingleNoteWidgetConfigureActivity : Activity()
{
    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    //lateinit internal var mAppWidgetText: EditText


    public override fun onCreate(icicle: Bundle?)
    {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(Activity.RESULT_CANCELED)

        setContentView(R.layout.single_note_widget_configure)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

//        val dbHelper = NotesDbHelper(this)
//        val db = dbHelper.writableDatabase
//
//        val layoutManager = GridLayoutManager(this, 2)
//        rv_notes.layoutManager = layoutManager
//
//        val notes = NotesDbHelper.getNotesFromDb(db)
//        val adapter = NotesAdapter(
//            this,
//            notes,
//            { noteLongClicked() },
//            { note: Note -> noteClicked(note) })
//        rv_notes.adapter = adapter
//
//        // If this activity was started with an intent without an app widget ID, finish with an error.
//        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
//        {
//            finish()
//            return
//        }


    }

    fun noteClicked(note: Note)
    {
        val context = this@SingleNoteWidgetConfigureActivity

        // When the button is clicked, store the string locally

        saveTitlePref(context, mAppWidgetId, note.text)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        SingleNoteWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId)

        // Make sure we pass back the original appWidgetId
        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    fun noteLongClicked() : Boolean
    {
        return false
    }

    companion object
    {

        private val PREFS_NAME = "com.andrius.notesappdemo.SingleNoteWidget"
        private val PREF_PREFIX_KEY = "appwidget_"

        // Write the prefix to the SharedPreferences object for this widget
        internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String)
        {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
            prefs.apply()
        }

        // Read the prefix from the SharedPreferences object for this widget.
        // If there is no preference saved, get the default from a resource
        internal fun loadTitlePref(context: Context, appWidgetId: Int): String
        {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
            return titleValue ?: context.getString(R.string.appwidget_text)
        }


        internal fun deleteTitlePref(context: Context, appWidgetId: Int)
        {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + appWidgetId)
            prefs.apply()
        }
    }
}

