package com.example.notes_app_lab1.view_model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.notes_app_lab1.app
import com.example.notes_app_lab1.data.dao.NoteDao
import com.example.notes_app_lab1.data.database.NoteDatabase
import com.example.notes_app_lab1.data.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteViewModel(): ViewModel() {
    private val database = NoteDatabase.getInstance(app.context!!)
    private val noteDao = database.noteDao()

    private val _state: MutableState<List<Note>> = mutableStateOf(emptyList())
    val state: State<List<Note>> = _state

    private fun getAll(){

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = noteDao.getAllNotesSortedByDate()
        }
    }

    fun getAllNotesSortedByDate() = viewModelScope.launch(Dispatchers.IO) {
        _state.value = noteDao.getAllNotesSortedByDate()
    }

    fun searchNoteByLabel(label: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.value = noteDao.getAllNotesFoundedByLabel(label)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.insetNote(note)
        getAll()
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.delete(note)
        getAll()
    }
}