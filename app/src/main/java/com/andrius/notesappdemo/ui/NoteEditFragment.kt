package com.andrius.notesappdemo.ui

import android.os.Bundle
import android.transition.TransitionInflater
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_newnote.view.*
import java.util.*

class NoteEditFragment : Fragment() {
    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = 0

    private lateinit var btnSave: FloatingActionButton
    private lateinit var etNote: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        noteId = NoteEditFragmentArgs.fromBundle(requireArguments()).noteId

        requireActivity().title = "Note editing"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_newnote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave = view.btn_add_note
        etNote = view.et_note_text.editText!!

        viewModel.notesObservable.observe(viewLifecycleOwner, Observer { notesMap ->
            if (notesMap.containsKey(noteId)) {

                etNote.setText(notesMap[noteId]!!.text)
            }

            btnSave.setOnClickListener {

                val lastModified = Date().toFormattedString()

                if (noteId >= 0) {
                    val note = notesMap[noteId]!!
                    note.text = etNote.text.toString()
                    note.lastModified = lastModified
                    viewModel.updateNote(note)
                } else {
                    viewModel.addNote(
                        Note(null, etNote.text.toString(),
                            0, lastModified, lastModified,
                        resources.getColor(R.color.colorPrimary))
                    )
                }

                findNavController().popBackStack()
            }
        })
    }


}