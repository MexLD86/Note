package com.example.note.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.note.data.Note
import com.example.note.data.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepository(application.applicationContext)

    private val _notes = MutableLiveData<List<Note>>(emptyList())
    val notes: LiveData<List<Note>> = _notes

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            val noteList = repository.getAllNotes()
            android.util.Log.d("VM_CHECK", "loadNotes: получил ${noteList.size} заметок")
            _notes.postValue(noteList)
        }
    }

    fun addNote(text:String) {
        viewModelScope.launch {
            val note = Note(
                text = text,
                isChecked = false
            )
            repository.insertNote(note)
            loadNotes()
        }
    }

    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
            loadNotes()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
            loadNotes()
        }
    }
}