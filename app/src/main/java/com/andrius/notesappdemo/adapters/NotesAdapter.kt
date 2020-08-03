package com.andrius.notesappdemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrius.notesappdemo.INote
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.models.Note
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_note.view.*
import kotlin.reflect.KClass

class NotesAdapter (var notes: MutableList<Note>, val callback: INote?, var context: Context) : RecyclerView.Adapter<MyViewHolder>()
{
    var widgetEnabled: Boolean = false
    lateinit var clickListener: (Note) -> Unit

    lateinit var myViewHolder: MyViewHolder
    lateinit var myViewHolderObject: KClass<out MyViewHolder>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)

        myViewHolder = MyViewHolder(view)
        myViewHolderObject = myViewHolder::class

        return myViewHolder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val note = notes[position]
        if(widgetEnabled) {
            holder.setDataForWidget(note, position, clickListener)
        }
        else {
            holder.tvText.text = note.text

            val menu = PopupMenu(context, holder.mcvNote)
            val inflater = menu.menuInflater

            holder.llNote.setOnLongClickListener {
                menu.menu.clear()
                inflater.inflate(R.menu.note_menu, menu.menu)
                menu.show()

                true
            }

            holder.llNote.setOnClickListener {
                callback?.onEditPressed(note, position)
            }

            menu.setOnMenuItemClickListener { menuItem ->

                when(menuItem.itemId) {
                    R.id.option_delete -> {
                        callback?.onDeletePressed(note, position)
                    }
                    R.id.option_edit -> {
                        callback?.onEditPressed(note, position)
                    }
                    else -> {}

                }

                true
            }
        }

    }
}

open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{

    lateinit var currentNote: Note

    var tvText: TextView = itemView.tv_note_text
    var mcvNote: MaterialCardView = itemView.mcv_note
    var llNote: LinearLayout = itemView.ll_note

    fun setDataForWidget(note: Note?, position: Int, clickListener: (Note) -> Unit) {
        currentNote = note!!
        itemView.tv_note_text.text = note.text
        note.sequence = position

        itemView.setOnClickListener{clickListener(note)}
    }
}


