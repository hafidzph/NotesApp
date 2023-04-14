package com.challenge.challengechapter4.data.ui.viewmodel

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.challengechapter4.data.local.dao.NoteDao
import com.challenge.challengechapter4.data.local.data.Notes
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao, app: Application): AndroidViewModel(app) {
    fun insert(note: Notes) = viewModelScope.launch {
        noteDao.insert(note)
    }

    fun updateTitle(id: Int, title: String) = viewModelScope.launch {
        noteDao.updateTitle(id, title)
    }

    fun updateNote(id: Int, note: String) = viewModelScope.launch {
        noteDao.updateNote(id, note)
    }

    fun updateAll(id: Int, title: String, note: String) = viewModelScope.launch {
        noteDao.update(id, title, note)
    }

    fun delete(noteId: Int) = viewModelScope.launch {
        noteDao.delete(noteId)
    }

    fun getAllNotes(userId: Int): LiveData<List<Notes>> {
        return noteDao.getAllNotes(userId)
    }

    fun getAllNotesAsc(userId: Int): LiveData<List<Notes>>{
        return noteDao.getAllNotesAsc(userId)
    }

    fun getAllNotesDesc(userId: Int): LiveData<List<Notes>>{
        return noteDao.getAllNotesDesc(userId)
    }
}