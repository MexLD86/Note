package com.example.note.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val context: Context
) {
    private val dbHelper = DatabaseHelper(context)

    suspend fun getAllNotes(): List<Note> = withContext(Dispatchers.IO) {
        dbHelper.getAllNotes()
    }

    suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.IO) {
        dbHelper.getNoteById(id)
    }

    suspend fun insertNote(note: Note): Long = withContext(Dispatchers.IO) {
       dbHelper.insertNote(note)
    }

    suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
        dbHelper.updateNote(note)
    }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteNote(id)
    }
}