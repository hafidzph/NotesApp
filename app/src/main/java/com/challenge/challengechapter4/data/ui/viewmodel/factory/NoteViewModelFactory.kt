package com.challenge.challengechapter4.data.ui.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.challengechapter4.data.local.dao.NoteDao
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel


class NoteViewModelFactory(
    private val dataSource: NoteDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}