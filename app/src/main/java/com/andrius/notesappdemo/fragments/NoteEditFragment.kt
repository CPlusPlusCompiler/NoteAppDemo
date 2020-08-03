package com.andrius.notesappdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.andrius.notesappdemo.NotesViewModel
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.models.Note
import com.andrius.notesappdemo.util.toFormattedString
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_newnote.view.*
import java.util.*

class NoteEditFragment : Fragment()
{
    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = 0

    private lateinit var btnSave: MaterialButton
    private lateinit var etNote: EditText

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

        btnSave = view.btn_add_note
        etNote = view.et_note_text.editText!!


        btnSave.setOnClickListener {


        }

        viewModel.notesObservable.observe(viewLifecycleOwner, Observer { notesMap ->
            if(notesMap.containsKey(noteId)) {

                etNote.setText(notesMap[noteId]!!.text)

                btnSave.setOnClickListener {

                    val lastModified = Date().toFormattedString()

                    if(noteId >= 0) {
                        notesMap[noteId]!!.text = etNote.text.toString()
                        notesMap[noteId]!!.lastModified = lastModified
                        viewModel.updateNote(notesMap[noteId]!!)
                    }
                    else {

                        viewModel.addNote(Note(null, etNote.text.toString(), 0, lastModified, lastModified))
                    }

                    findNavController().popBackStack()


                }
            }
        })
    }


}