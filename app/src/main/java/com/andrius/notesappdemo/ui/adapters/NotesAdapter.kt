package com.andrius.notesappdemo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.andrius.notesappdemo.interfaces.INoteAction
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.interfaces.INoteClick
import com.andrius.notesappdemo.models.Note
import com.andrius.notesappdemo.util.SortOrder
import com.andrius.notesappdemo.util.toDate
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.item_note_widget.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesAdapter(
    private var notes: MutableList<Note>,
    var noteType: Note.ViewType,
    private val clickCallback: INoteClick?,
    private val actionCallback: INoteAction?,
    private var context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return  if (noteType == Note.ViewType.IN_APP)
            MyViewHolder(
                LayoutInflater.from(
                    context
                ).inflate(R.layout.item_note, parent, false)
            )
        else
            WidgetViewHolder(
                LayoutInflater.from(
                    context
                ).inflate(R.layout.item_note_widget, parent, false)
            )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val note = notes[position]

        if (holder is MyViewHolder) {

            holder.tvText.text = note.text
            holder.tvLastModified.text = note.lastModified

            holder.clNote.setOnLongClickListener {
                clickCallback?.onLongClicked(note, position)
                true
            }

            holder.imgSelected.visibility = if (note.isSelected)
                View.VISIBLE
            else
                View.GONE

            holder.clNote.setOnClickListener {
                clickCallback?.onClicked(note, position)
            }

            // set up menu
            val menu = PopupMenu(context, holder.mcvNote)
            val inflater = menu.menuInflater

            // todo save it for dot menu
//            menu.menu.clear()
//            menu.show()
//            inflater.inflate(R.menu.note_menu, menu.menu)

            menu.setOnMenuItemClickListener { menuItem ->

                when (menuItem.itemId) {
                    R.id.option_delete -> {
                        actionCallback?.onDeletePressed(note, position)
                    }
                    R.id.option_edit -> {
                        actionCallback?.onEditPressed(note, position)
                    }
                    else -> { }
                }

                true
            }
        }
        else if (holder is WidgetViewHolder) {

            holder.tvText.text = note.text
            note.sequence = position

            holder.rlNote.setOnClickListener {
                clickCallback?.onClicked(note, position)
            }
        }

    }

    fun isAnySelected(): Boolean {

        notes.forEach {
            if (it.isSelected)
                return true
        }

        return false
    }

    fun getSelectedNoteIds(): List<Long> {

        val selectedIds = mutableListOf<Long>()

        notes.forEach {
            if (it.isSelected)
                selectedIds.add(it.id!!)
        }

        return selectedIds
    }

    @Deprecated("sort the data, fool")
    fun sort(sortOrder: SortOrder) {

        CoroutineScope(Dispatchers.Default).launch {
            when (sortOrder) {
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

open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    var tvText: TextView = itemView.tv_note_text
    var mcvNote: CardView = itemView.mcv_note
    var clNote: ConstraintLayout = itemView.cl_note
    var imgSelected: ImageView = itemView.img_selected
    var tvLastModified: TextView = itemView.tv_last_modified
}

open class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvText: TextView = itemView.widget_text
    var rlNote: RelativeLayout = itemView.rl_note

    init {


    }


}


