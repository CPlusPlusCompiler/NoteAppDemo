package com.andrius.notesappdemo

import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.storage.FileDownloadTask
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import com.google.firebase.storage.UploadTask


class MainActivity : AppCompatActivity()
{
    var selectedNotePosition: Int = 0

//    val adapter = getAdapter(this, Supplier.userNotes)

    val KEY_NEW_NOTE = "user_note"
    val KEY_ID = "id_note"
    val KEY_EDITED_NOTE = "edit_note"
    val KEY_NOTE_POS = "note_position"
    val KEY_SELECT_NOTE = "note_select"

    val FILE_PATH_WITH_NAME = "data/data/com.andrius.notesappdemo/databases/DbNotes.db"
    val FILE_PATH = "data/data/com.andrius.notesappdemo/databases"


    lateinit var file: Uri
//    lateinit var uploadTask: UploadTask
//    lateinit var dbFileRef: StorageReference
//    lateinit var storage: FirebaseStorage

    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        storage = FirebaseStorage.getInstance()

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

//                var storageRef = storage.reference
//
//                val dbRef = storageRef.child("DbNotes.db")
//                dbFileRef = storageRef.child("data/DbNotes.db")
//
//                file = Uri.fromFile(File(FILE_PATH_WITH_NAME))
//                uploadTask = dbFileRef.putFile(file)
//
//                uploadTask.addOnFailureListener()
//                {
//                    onUploadFailure(it)
//                }
//                uploadTask.addOnSuccessListener()
//                {
//                    onUploadSuccess(it)
//                }

                true
            }
            R.id.menu_restore ->
            {
//                val storageRef = storage.reference
//
//                val pathRef = storageRef.child("data/DbNotes.db")
//                val gsRef = storageRef.child("gs://bucket/data/DbNotes.db")
//
//                dbFileRef = storageRef.child("data/DbNotes.db")
//                val localFile = File(FILE_PATH, "DbNotes.db")
//
//
//                dbFileRef.getFile(localFile).addOnSuccessListener()
//                {
//                    onDownloadSuccess(it)
//
//                    adapter.notes.clear()
//                    db = NotesDbHelper(this).writableDatabase
//
//                    val tempNotes = NotesDbHelper.getNotesFromDb(db)
//
//                    for(i in tempNotes)
//                    {
//                        adapter.notes.add(i)
//                    }
//
//                    db.close()
//                    adapter.notifyDataSetChanged()
//
//                }
//                    .addOnFailureListener()
//                    {
//                        onDownloadFailure(it)
//                    }

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
//            R.id.option_delete ->
//            {
////                adapter.deleteNote(selectedNotePosition)
////                adapter.notifyDataSetChanged()
////                true
//            }
            R.id.option_edit ->
            {
//                val intent = Intent(this, `EditNoteFragment()`::class.java)
//                intent.putExtra(KEY_EDITED_NOTE, adapter.notes[selectedNotePosition].text)
//                intent.putExtra(KEY_NOTE_POS, selectedNotePosition.toString())
//                intent.putExtra(KEY_ID, adapter.notes[selectedNotePosition].dbId.toString())


//                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


//    fun getAdapter(context: Context, notes: MutableList<Note>) : NotesAdapter
//    {
//        val tempAdapter = NotesAdapter(
//            context,
//            notes,
//            { note: Note -> noteLongClicked(note) })
//        return tempAdapter
//    }

}
