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

class NotesAdapter (var context: Context, var notes: MutableList<Note>, val longClickListener: (Note) -> Boolean) : RecyclerView.Adapter<MyViewHolder>()
{

    var widgetEnabled: Boolean = false
    lateinit var clickListener: (Note) -> Unit

    constructor(context: Context, notes: MutableList<Note>, longClickListener: (Note) -> Boolean, clickListener: (Note) -> Unit) : this(context, notes, longClickListener)
    {
        widgetEnabled = true
        this.clickListener = clickListener
    }


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
        if(widgetEnabled)
        {
            (holder as MyViewHolder).setDataForWidget(note, position, clickListener)
        }
        else
        {
            (holder as MyViewHolder).setData(note, position, longClickListener)
        }

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


        val tempDb = notesDbHelper.writableDatabase
        val id = MyViewHolder::class

        tempDb.execSQL("DELETE FROM ${NotesContract.NoteEntry.TABLE_NAME} WHERE ${BaseColumns._ID} = ${notes[position].dbId}")
        tempDb.close()
        notes.removeAt(position)

    }

}

open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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

    fun setDataForWidget(note: Note?, position: Int, clickListener: (Note) -> Unit)
    {
        currentNote = note!!
        itemView.noteText.text = note!!.text
        note.position = position
        note.itemView = itemView

        itemView.setOnClickListener{clickListener(note)}
    }


}


