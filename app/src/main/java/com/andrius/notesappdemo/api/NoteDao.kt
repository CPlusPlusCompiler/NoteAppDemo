package com.andrius.notesappdemo.api

import androidx.room.*
import com.andrius.notesappdemo.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: Note): Long

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note: Note)
}