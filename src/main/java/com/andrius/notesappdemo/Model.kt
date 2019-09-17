package com.andrius.notesappdemo

import android.view.View

data class Note (val text: String)
{
    var dbId: Long = 0
    var position: Int = 0
    lateinit var itemView: View

    constructor(text: String, dbId: Long, position: Int) : this(text)
    {
        this.dbId = dbId
        this.position = position
    }

    constructor(text: String, dbId: Long) : this(text)
    {
        this.dbId = dbId
    }
}

object Supplier
{
    var userNotes: MutableList<Note> = mutableListOf<Note>()
}