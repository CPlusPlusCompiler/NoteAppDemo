package com.andrius.notesappdemo.interfaces

import com.andrius.notesappdemo.util.SortOrder

interface INotesOperation {

    fun deleteSelectedNotes()
    fun sortNotes(sortBy: SortOrder)
}