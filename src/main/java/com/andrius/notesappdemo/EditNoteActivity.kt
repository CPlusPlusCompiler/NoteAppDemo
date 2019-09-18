package com.andrius.notesappdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*

class EditNoteActivity : AppCompatActivity()
{

    val KEY_ID: String = "id_note"
    val KEY_NOTE: String = "edit_note"
    val KEY_NOTE_POS = "note_position"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val notesDbHelper = NotesDbHelper(this)
        val db = notesDbHelper.writableDatabase


        val noteBundle = receiveNoteBundle()
        var id: String = ""
        var position: String = ""
        lateinit var text: String

        if(noteBundle != null)
        {
            if(!noteBundle.dbId.isNullOrEmpty())
            {
                id = noteBundle.dbId
                text = noteBundle.text
                position = noteBundle.position

                editNote.setText(text)


                btnSaveNote.setOnClickListener()
                {
                    text = editNote.text.toString()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(KEY_ID, id)
                    intent.putExtra(KEY_NOTE, text)
                    intent.putExtra(KEY_NOTE_POS, position)

                    startActivity(intent)
                }

            }
        }




    }

    fun receiveNoteBundle() : NoteBundle?
    {
        val bundle: Bundle? = intent.extras

        if(bundle != null)
        {
            val tempNote = bundle.getString(KEY_NOTE)!!
            val tempNoteId = bundle.getString(KEY_ID)!!
            val tempNotePos = bundle.getString(KEY_NOTE_POS)!!

            val tempList = mutableListOf(tempNoteId, tempNote, tempNotePos)

            return NoteBundle(KEY_NOTE, tempNote, tempNoteId, tempNotePos)
        }
        else
            return null

    }

}