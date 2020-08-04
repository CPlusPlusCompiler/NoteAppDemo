package com.andrius.notesappdemo.interfaces

import com.andrius.notesappdemo.models.Note

interface INote {
    fun onDeletePressed(note: Note, position: Int)
    fun onEditPressed(note: Note, position: Int)
    fun onLongClicked(note: Note, position: Int)
    fun onClicked(note: Note, position: Int)
}