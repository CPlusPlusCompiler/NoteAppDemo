package com.andrius.notesappdemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrius.notesappdemo.MainActivity
import com.andrius.notesappdemo.interfaces.INote
import com.andrius.notesappdemo.NotesViewModel
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.adapters.NotesAdapter
import com.andrius.notesappdemo.interfaces.INotesOperation
import com.andrius.notesappdemo.interfaces.INotesSelection
import com.andrius.notesappdemo.models.Note
import com.andrius.notesappdemo.util.SortOrder
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_notes.view.*

class NotesListFragment: Fragment(), INotesOperation {

    private lateinit var viewModel: NotesViewModel
    private lateinit var notesSelectionCallback: INotesSelection
    private var isSelectionOn = false
    private lateinit var rvNotes: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val mainActivity = requireActivity() as MainActivity
        notesSelectionCallback = mainActivity
        mainActivity.setNoteDeletionCallback(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)

        if(viewModel.notesObservable.value.isNullOrEmpty())
            viewModel.loadNotes()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvNotes = view.rv_notes

        btn_add.setOnClickListener {
            findNavController().navigate (
                NotesListFragmentDirections.actionNotesListFragmentToNewNoteFragment(-1))
        }

        viewModel.notesObservable.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.values.toMutableList())
        })
    }

    fun initRecyclerView(notes: MutableList<Note>) {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvNotes.layoutManager = layoutManager
        rvNotes.adapter = NotesAdapter(notes, notesListener, requireContext())
        registerForContextMenu(rvNotes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv_notes.adapter = null
    }

    val notesListener = object: INote {
        override fun onDeletePressed(note: Note, position: Int) {
            viewModel.deleteNotes(listOf(note.id!!))
        }

        override fun onEditPressed(note: Note, position: Int) {

            val noteId: Long = if(note.id == null)
                -1
            else
                note.id!!

            findNavController().navigate (
                NotesListFragmentDirections.actionNotesListFragmentToNewNoteFragment(noteId)
            )
        }

        override fun onLongClicked(note: Note, position: Int) {
            isSelectionOn = true
            notesSelectionCallback.startSelection()
        }

        override fun onClicked(note: Note, position: Int) {

            if(isSelectionOn) {
                note.isSelected = !note.isSelected

                val adapter = rvNotes.adapter as NotesAdapter
                adapter.notifyItemChanged(position)

                if(!adapter.isAnySelected()) {
                    isSelectionOn = false
                    notesSelectionCallback.stopSelection()
                }
            }
            else
                onEditPressed(note, position)
        }
    }

    override fun deleteSelectedNotes() {

        notesSelectionCallback.stopSelection()

        val adapter = (rvNotes.adapter as NotesAdapter)
        viewModel.deleteNotes(adapter.getSelectedNoteIds())
    }

    override fun sortNotes(sortOrder: SortOrder) {

        (rvNotes.adapter as NotesAdapter).sort(sortOrder)
    }

}