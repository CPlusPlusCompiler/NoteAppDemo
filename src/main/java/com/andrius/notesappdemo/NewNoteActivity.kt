package com.andrius.notesappdemo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_newnote.*

class NewNoteActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newnote)


        btnAddNote.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            val note: String = editNote.text.toString()

            intent.putExtra("user_note", note)

            startActivity(intent)
        }
    }
}