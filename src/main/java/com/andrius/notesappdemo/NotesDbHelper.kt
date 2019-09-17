package com.andrius.notesappdemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

const val DATABASE_NAME = "DbNotes.db"
const val DATABASE_VERSION = 1

object NotesContract
{

    object NoteEntry : BaseColumns
    {
        const val TABLE_NAME = "notes"
        const val COLUMN_NAME_TITLE = "contents"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${NotesContract.NoteEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${NotesContract.NoteEntry.COLUMN_NAME_TITLE} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${NotesContract.NoteEntry.TABLE_NAME}"

class NotesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_ENTRIES)
        //onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //db.execSQL(SQL_DELETE_ENTRIES)
    }



    companion object
    {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "DbNotes.db"
    }
}