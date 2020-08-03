package com.andrius.notesappdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.andrius.notesappdemo.api.Database
import com.andrius.notesappdemo.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        application.applicationContext,
        Database::class.java, Database.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    val notesObservable = MutableLiveData<SortedMap<Long, Note>>()

    fun loadNotes() {

        viewModelScope.launch(Dispatchers.IO) {

            val list = database.noteDao().getAll()

            val notesMap = sortedMapOf<Long, Note>()

            list.forEach { note ->
                notesMap[note.id] = note
            }

            withContext(Dispatchers.Main) {
                notesObservable.value = notesMap
            }
        }
    }

    fun addNote(note: Note) {

        viewModelScope.launch(Dispatchers.IO) {

            val id = database.noteDao().insert(note)

            withContext(Dispatchers.Main) {
                // todo if adding was successful
                notesObservable.value!![id] = note
                // notify observers
                notesObservable.value = notesObservable.value
            }
        }
    }

    fun updateNote(note: Note) {

        viewModelScope.launch(Dispatchers.IO) {
            database.noteDao().update(note)

            withContext(Dispatchers.Main) {
                notesObservable.value!![note.id] = note
                notesObservable.value = notesObservable.value
            }
        }
    }

    fun deleteNote(note: Note) {

        viewModelScope.launch (Dispatchers.IO) {

            database.noteDao().delete(note)

            withContext(Dispatchers.Main) {
                notesObservable.value?.remove(note.id)
                notesObservable.value = notesObservable.value
            }

        }

    }

}
