package com.andrius.notesappdemo.interfaces

import com.andrius.notesappdemo.models.Note

interface INoteAction {
    fun onDeletePressed(note: Note, position: Int)
    fun onEditPressed(note: Note, position: Int)

}