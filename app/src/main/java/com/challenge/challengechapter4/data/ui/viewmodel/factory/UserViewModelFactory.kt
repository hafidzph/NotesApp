package com.challenge.challengechapter4.data.ui.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.challengechapter4.data.local.dao.UserDao
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import com.challenge.challengechapter4.data.ui.viewmodel.UserViewModel

class UserViewModelFactory(
    private val dataSource: UserDao,
    private val application: Application,
    private val prefs : UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(dataSource, application, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
