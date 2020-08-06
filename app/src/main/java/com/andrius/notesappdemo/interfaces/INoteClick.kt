package com.andrius.notesappdemo.interfaces

import com.andrius.notesappdemo.models.Note

interface INoteClick {
    fun onLongClicked(note: Note, position: Int)
    fun onClicked(note: Note, position: Int)
}