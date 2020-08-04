package com.andrius.notesappdemo.api

import androidx.room.*
import com.andrius.notesappdemo.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Insert()
    fun insert(notes: Note): Long

    @Query ("DELETE FROM Note where id in (:idList)")
    fun delete(idList: List<Long>)

    @Update
    fun update(note: Note)
}