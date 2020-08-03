package com.andrius.notesappdemo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity()
{
    var selectedNotePosition: Int = 0

    val adapter = getAdapter(this, Supplier.userNotes)

    val KEY_NEW_NOTE = "user_note"
    val KEY_ID = "id_note"
    val KEY_EDITED_NOTE = "edit_note"
    val KEY_NOTE_POS = "note_position"
    val KEY_SELECT_NOTE = "note_select"

    val FILE_PATH_WITH_NAME = "data/data/com.andrius.notesappdemo/databases/DbNotes.db"
    val FILE_PATH = "data/data/com.andrius.notesappdemo/databases"


    lateinit var file: Uri
    lateinit var uploadTask: UploadTask
    lateinit var dbFileRef: StorageReference
    lateinit var storage: FirebaseStorage

    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //storageReference = FirebaseStorage.getInstance().getReference()

        //val file = Uri.fromFile(File("data/data/com.andrius.notesappdemo/databases/DbNotes.db"))

        storage = FirebaseStorage.getInstance()

        btnAdd.setOnClickListener()
        {
            val intent = Intent(this, NewNoteActivity::class.java)

            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this, 2)


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)


        // loading and saving notes
        //-------------------------------------------------------------------------------------------
        val notesDbHelper = NotesDbHelper(this)
        db = notesDbHelper.writableDatabase


        var newRowId: Long = 0                                      // for the database
        val noteBundle = receiveNoteBundle()


        if(noteBundle != null)                                      // if a bundle is received
        {
            when(noteBundle.key)
            {
                KEY_NEW_NOTE ->                                     // add a new note to the db
                {
                    val values = ContentValues().apply()
                    {
                        put(NotesContract.NoteEntry.COLUMN_NAME_TITLE, noteBundle.text)
                    }

                    newRowId = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, values)
                    adapter.notes.add(Note(noteBundle.text, newRowId))

                    db.close()
                }
                KEY_EDITED_NOTE->                                   // edit a note in the db
                {
                    db.execSQL("UPDATE ${NotesContract.NoteEntry.TABLE_NAME} SET ${NotesContract.NoteEntry.COLUMN_NAME_TITLE} = '${noteBundle.text}' WHERE ${BaseColumns._ID} = ${noteBundle.dbId}")
                    db.close()

                    adapter.notes.set(noteBundle.position.toInt(), Note(noteBundle.text))
                    adapter.notifyDataSetChanged()
                }
            }
        }
        else                                                    // if not, then load notes from db
        {
            val tempNotes = NotesDbHelper.getNotesFromDb(db)

            for(i in tempNotes)
            {
                adapter.notes.add(i)
            }

            db.close()
        }
        //------------------------------------------------------------------------------------------
    }

    fun noteLongClicked(note: Note) : Boolean
    {
        selectedNotePosition = note.position
        note.itemView.showContextMenu()
        return true
    }

    fun onUploadFailure(e: Exception)
    {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }

    fun onUploadSuccess(task: UploadTask.TaskSnapshot)
    {

        Toast.makeText(this, "Upload success!", Toast.LENGTH_SHORT).show()
    }

    fun onDownloadFailure(e: Exception)
    {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }

    fun onDownloadSuccess(task: FileDownloadTask.TaskSnapshot)
    {


        Toast.makeText(this, "Restore success", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
        //return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when(item.itemId)
        {
            R.id.menu_back_up ->
            {

                var storageRef = storage.reference

                val dbRef = storageRef.child("DbNotes.db")
                dbFileRef = storageRef.child("data/DbNotes.db")

                file = Uri.fromFile(File(FILE_PATH_WITH_NAME))
                uploadTask = dbFileRef.putFile(file)

                uploadTask.addOnFailureListener()
                {
                    onUploadFailure(it)
                }
                uploadTask.addOnSuccessListener()
                {
                    onUploadSuccess(it)
                }

                true
            }
            R.id.menu_restore ->
            {
                val storageRef = storage.reference

                val pathRef = storageRef.child("data/DbNotes.db")
                val gsRef = storageRef.child("gs://bucket/data/DbNotes.db")

                dbFileRef = storageRef.child("data/DbNotes.db")
                val localFile = File(FILE_PATH, "DbNotes.db")


                dbFileRef.getFile(localFile).addOnSuccessListener()
                {
                    onDownloadSuccess(it)

                    adapter.notes.clear()
                    db = NotesDbHelper(this).writableDatabase

                    val tempNotes = NotesDbHelper.getNotesFromDb(db)

                    for(i in tempNotes)
                    {
                        adapter.notes.add(i)
                    }

                    db.close()
                    adapter.notifyDataSetChanged()

                }
                    .addOnFailureListener()
                    {
                        onDownloadFailure(it)
                    }

                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }


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
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra(KEY_EDITED_NOTE, adapter.notes[selectedNotePosition].text)
                intent.putExtra(KEY_NOTE_POS, selectedNotePosition.toString())
                intent.putExtra(KEY_ID, adapter.notes[selectedNotePosition].dbId.toString())


                startActivity(intent)
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


    fun receiveNoteBundle() : NoteBundle?
    {
        val bundle: Bundle? = intent.extras

        if(bundle != null)
        {

            if(!bundle.getString(KEY_NEW_NOTE).isNullOrEmpty())
            {
                val noteText = bundle.getString(KEY_NEW_NOTE)!!
                val key = KEY_NEW_NOTE

                return NoteBundle(key, noteText)
            }
            else if(!bundle.getString(KEY_EDITED_NOTE).isNullOrEmpty())
            {
                val noteText = bundle.getString(KEY_EDITED_NOTE)!!
                val id = bundle.getString(KEY_ID)!!
                val key = KEY_EDITED_NOTE
                val position = bundle.getString(KEY_NOTE_POS)!!

                return NoteBundle(key, noteText, id, position)
            }
            else if(!bundle.getString(KEY_SELECT_NOTE).isNullOrEmpty())
            {
                val noteText = bundle.getString(KEY_SELECT_NOTE)!!
                val key = KEY_SELECT_NOTE

                return NoteBundle(key, noteText)
            }
            else
                return null

        }
        else
            return null

    }
}
