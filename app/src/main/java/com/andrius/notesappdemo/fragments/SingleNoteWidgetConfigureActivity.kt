package com.andrius.notesappdemo.fragments

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.andrius.notesappdemo.NotesViewModel
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.adapters.NotesAdapter
import com.andrius.notesappdemo.interfaces.INote
import com.andrius.notesappdemo.interfaces.INotesOperation
import com.andrius.notesappdemo.interfaces.INotesSelection
import com.andrius.notesappdemo.models.Note
import kotlinx.android.synthetic.main.single_note_widget_configure.*

/**
 * The configuration screen for the [SingleNoteWidget] AppWidget.
 */
class SingleNoteWidgetConfigureActivity : AppCompatActivity()
{
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var viewModel: NotesViewModel

    public override fun onCreate(icicle: Bundle?)
    {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(Activity.RESULT_CANCELED)

        setContentView(R.layout.single_note_widget_configure)

        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        // Find the widget id from the intent.
        val extras = intent.extras

        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        viewModel.loadNotes()

        viewModel.notesObservable.observe(this, Observer {
            initRecyclerView(it.values.toList())
        })
    }

    fun initRecyclerView(notes: List<Note>) {

        rv_notes.adapter = NotesAdapter(notes as MutableList<Note>, noteListener, baseContext)
        rv_notes.layoutManager = GridLayoutManager(baseContext, 2)

    }

    // todo make another interface, jesus
    private val noteListener = object: INote {
        override fun onDeletePressed(note: Note, position: Int) { }

        override fun onEditPressed(note: Note, position: Int) { }

        override fun onLongClicked(note: Note, position: Int) { }

        override fun onClicked(note: Note, position: Int) {
            selectNote(note)
        }

    }

    fun selectNote(note: Note)
    {
        val context = this@SingleNoteWidgetConfigureActivity

        // When the button is clicked, store the string locally

        saveTitlePref(
            context,
            appWidgetId,
            note.text
        )

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        SingleNoteWidget.updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    companion object {

        private val PREFS_NAME = "com.andrius.notesappdemo.fragments.SingleNoteWidget"
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

