package com.andrius.notesappdemo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{
    var selectedNotePosition: Int = 0

    val adapter = getAdapter(this, Supplier.userNotes)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd.setOnClickListener()
        {
            val intent = Intent(this, NewNoteActivity::class.java)

            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this, 2)


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)




        val notesDbHelper = NotesDbHelper(this)
        val db = notesDbHelper.writableDatabase


        var newRowId: Long = 0                                  // for the database
        val noteString = receiveNoteBundle()

        if(!noteString.isNullOrEmpty())                         // if a bundle is received
        {


            val values = ContentValues().apply()
            {
                put(NotesContract.NoteEntry.COLUMN_NAME_TITLE, noteString)

            }

            getNotesFromDb(db)

            newRowId = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, values)
            adapter.notes.add(Note(noteString, newRowId))


            db.close()
        }
        else                                                    // if not, then load notes from db
        {
            val tempNotes = getNotesFromDb(db)

            for(i in tempNotes)
            {
                adapter.notes.add(i)
            }

            db.close()
        }
    }

    fun noteLongClicked(note: Note) : Boolean
    {
        Toast.makeText(this, "note database id: " + note.dbId, Toast.LENGTH_SHORT).show()
        selectedNotePosition = note.position
        note.itemView.showContextMenu()
        return true
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        menuInflater.inflate(R.menu.note_menu, menu)

        super.onCreateContextMenu(menu, v, menuInfo)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        return when(item.itemId)
        {
            R.id.option_delete ->
            {
                adapter.deleteNote(selectedNotePosition)
                adapter.notifyDataSetChanged()
                true
            }
            R.id.option_edit ->
            {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun getAdapter(context: Context, notes: MutableList<Note>) : NotesAdapter
    {
        val tempAdapter = NotesAdapter(context, notes, {note: Note -> noteLongClicked(note)})
        return tempAdapter
    }


    fun getNotesFromDb(db: SQLiteDatabase) : MutableList<Note>
    {
        val count = DatabaseUtils.queryNumEntries(db, NotesContract.NoteEntry.TABLE_NAME)
        Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show()

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


    fun receiveNoteBundle() : String?
    {
        val bundle: Bundle? = intent.extras

        if(bundle != null)
        {
            val tempNote = bundle.getString("user_note")
            Toast.makeText(this, tempNote, Toast.LENGTH_SHORT).show()

            return tempNote
        }
        else
            return null

    }
}
