package com.andrius.notesappdemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.andrius.notesappdemo.interfaces.INote
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.models.Note
import com.andrius.notesappdemo.util.SortOrder
import com.andrius.notesappdemo.util.toDate
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

            holder.clNote.setOnLongClickListener {
                callback?.onLongClicked(note, position)
                true
            }

            holder.imgSelected.visibility = if(note.isSelected)
                View.VISIBLE
            else
                View.GONE

            holder.clNote.setOnClickListener {
                callback?.onClicked(note, position)
            }

            // set up menu
            val menu = PopupMenu(context, holder.mcvNote)
            val inflater = menu.menuInflater

            // todo save it for dot menu
//            menu.menu.clear()
//            menu.show()
//            inflater.inflate(R.menu.note_menu, menu.menu)

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

    fun isAnySelected(): Boolean {

        notes.forEach {
            if(it.isSelected)
                return true
        }

        return false
    }

    fun deleteSelectedNotes(): List<Long> {

        val removedIds = mutableListOf<Long>()

        // iterator is used because otherwise ConcurrentModificationException
        // will be thrown
        val iterator = notes.iterator()

        while(iterator.hasNext()) {
            val note = iterator.next()

            if(note.isSelected) {
                removedIds.add(note.id!!)
                iterator.remove()
            }
        }

        notifyDataSetChanged()

        return removedIds
    }

    fun getSelectedNoteIds(): List<Long> {

        val selectedIds = mutableListOf<Long>()

        notes.forEach {
            if(it.isSelected)
                selectedIds.add(it.id!!)
        }

        return selectedIds
    }

    fun sort(sortOrder: SortOrder) {

        CoroutineScope(Dispatchers.Default).launch {
            when(sortOrder) {
                SortOrder.MODIFICATION_DATE ->
                    notes.sortBy { it.lastModified.toDate() }
                SortOrder.CREATION_DATE ->
                    notes.sortBy { it.dateCreated.toDate() }
                SortOrder.CONTENT ->
                    notes.sortBy { it.text }
            }

            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }

    }
}

open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    lateinit var currentNote: Note

    var tvText: TextView = itemView.tv_note_text
    var mcvNote: MaterialCardView = itemView.mcv_note
    var clNote: ConstraintLayout = itemView.cl_note
    var imgSelected: ImageView = itemView.img_selected

    fun setDataForWidget(note: Note?, position: Int, clickListener: (Note) -> Unit) {
        currentNote = note!!
        itemView.tv_note_text.text = note.text
        note.sequence = position

        itemView.setOnClickListener{clickListener(note)}
    }
}


