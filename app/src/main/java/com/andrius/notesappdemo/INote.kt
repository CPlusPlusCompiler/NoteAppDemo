package com.andrius.notesappdemo

import com.andrius.notesappdemo.models.Note

interface INote {
    fun onDeletePressed(note: Note, position: Int)
    fun onEditPressed(note: Note, position: Int)
}