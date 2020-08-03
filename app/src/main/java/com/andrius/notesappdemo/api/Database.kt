package com.andrius.notesappdemo.api

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrius.notesappdemo.models.Note

@Database(entities = [Note::class], version = 2)
abstract class Database: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "Notes.db"
    }
}