package com.andrius.notesappdemo

import android.view.View

data class Note (var text: String)
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

data class NoteBundle(val key: String, val text: String)
{
    var dbId: String = ""
    var position: String = ""

    constructor(key: String, text: String, dbId: String, position: String) : this(key, text)
    {
        this.dbId = dbId
        this.position = position
    }

}

object Supplier
{
    var userNotes: MutableList<Note> = mutableListOf<Note>()
}