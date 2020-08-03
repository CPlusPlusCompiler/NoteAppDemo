package com.andrius.notesappdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.andrius.notesappdemo.INote
import com.andrius.notesappdemo.NotesViewModel
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.adapters.NotesAdapter
import com.andrius.notesappdemo.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesListFragment: Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var btnAdd: FloatingActionButton

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

        btn_add.setOnClickListener {
            findNavController().navigate (
                NotesListFragmentDirections.actionNotesListFragmentToNewNoteFragment(-1))
        }

        viewModel.notesObservable.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.values.toMutableList())
        })

    }

    val notesListener = object: INote {
        override fun onDeletePressed(note: Note, position: Int) {
            viewModel.deleteNote(note)
        }

        override fun onEditPressed(note: Note, position: Int) {

            findNavController().navigate(
                NotesListFragmentDirections.actionNotesListFragmentToNewNoteFragment(note.id!!)
            )

        }
    }

    fun initRecyclerView(notes: MutableList<Note>) {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rv_notes.layoutManager = layoutManager
        rv_notes.adapter = NotesAdapter(notes, notesListener, requireContext())
        registerForContextMenu(rv_notes)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        rv_notes.adapter = null
    }

}