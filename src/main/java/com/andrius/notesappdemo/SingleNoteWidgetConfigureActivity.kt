package com.andrius.notesappdemo

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

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

        val dbHelper = NotesDbHelper(this)
        val db = dbHelper.writableDatabase

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        val notes = getNotesFromDb(db)
        val adapter = NotesAdapter(this, notes, {noteLongClicked()}, {note: Note -> noteClicked(note) })
        recyclerView.adapter = adapter
        
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish()
            return
        }

        //mAppWidgetText.setText(loadTitlePref(this@SingleNoteWidgetConfigureActivity, mAppWidgetId))
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

    fun getNotesFromDb(db: SQLiteDatabase) : MutableList<Note>
    {
        val count = DatabaseUtils.queryNumEntries(db, NotesContract.NoteEntry.TABLE_NAME)
        //Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show()

        val cursor = db.rawQuery("SELECT * FROM ${NotesContract.NoteEntry.TABLE_NAME}", null)

        val noteContents = mutableListOf<Note>()

        with(cursor)
        {
            while(moveToNext())
            {
                val noteContent = cursor.getString(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_TITLE))
                val noteId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                noteContents.add(Note(noteContent, noteId))
            }
        }

        return noteContents
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

