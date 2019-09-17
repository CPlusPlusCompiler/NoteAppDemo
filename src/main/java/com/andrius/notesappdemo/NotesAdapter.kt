package com.andrius.notesappdemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_note.view.*
import kotlin.reflect.KClass

class NotesAdapter(val context: Context, var notes: MutableList<Note>, val clickListener: (Note) -> Boolean) : RecyclerView.Adapter<MyViewHolder>()
{

    lateinit var myViewHolder: MyViewHolder
    lateinit var myViewHolderObject: KClass<out MyViewHolder>
    val notesDbHelper = NotesDbHelper(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)

        myViewHolder = MyViewHolder(view)
        myViewHolderObject = myViewHolder::class

        return myViewHolder
    }

    override fun getItemCount(): Int
    {
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val note = notes[position]
        (holder as MyViewHolder).setData(note, position, clickListener)

        //myViewHolder = holder
        //myViewHolderObject = holder::class
//
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun getDb() : SQLiteDatabase
    {
        val dbHelper = NotesDbHelper(context)
        return dbHelper.writableDatabase
    }

    fun deleteNote(position: Int)
    {
        //Toast.makeText(this, selectedNoteId.toString(), Toast.LENGTH_SHORT).show()

        val tempDb = notesDbHelper.writableDatabase
        val id = MyViewHolder::class

        tempDb.execSQL("DELETE FROM ${NotesContract.NoteEntry.TABLE_NAME} WHERE ${BaseColumns._ID} = ${notes[position].dbId}")
        tempDb.close()
        notes.removeAt(position)

    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{

    lateinit var currentNote: Note
    //var currentPosition: Int by Delegates.notNull<Int>()

    fun setData(note: Note?, position: Int, clickListener: (Note) -> Boolean)
    {

        currentNote = note!!
        itemView.noteText.text = note!!.text
        note.position = position
        note.itemView = itemView

        itemView.setOnLongClickListener{clickListener(note)}
    }



}


