package com.andrius.notesappdemo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrius.notesappdemo.MainActivity
import com.andrius.notesappdemo.NotesViewModel
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.models.Note
import kotlinx.android.synthetic.main.fragment_newnote.*
import kotlinx.android.synthetic.main.fragment_newnote.view.*
import kotlinx.android.synthetic.main.list_item_note.*

class NoteEditFragment : Fragment()
{
    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        noteId = NoteEditFragmentArgs.fromBundle(requireArguments()).noteId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        return inflater.inflate(R.layout.fragment_newnote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_note.setOnClickListener {
            viewModel.addNote(Note(null, view.et_note_text.editText!!.text.toString(), 0))
            findNavController().popBackStack()
        }

        viewModel.notesObservable.observe(viewLifecycleOwner, Observer { notesMap ->
            if(noteId >= 0 && notesMap.containsKey(noteId)) {

                et_note_text.editText!!.setText(notesMap[noteId]!!.text)

                btn_add_note.setOnClickListener {
                    notesMap[noteId]!!.text = et_note_text.editText!!.text.toString()
                    viewModel.updateNote(notesMap[noteId]!!)
                    findNavController().popBackStack()
                }
            }
        })
    }


}