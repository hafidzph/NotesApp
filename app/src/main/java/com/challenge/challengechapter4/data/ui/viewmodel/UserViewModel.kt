package com.challenge.challengechapter4.data.ui.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.dao.UserDao
import com.challenge.challengechapter4.data.local.data.Users
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userDao: UserDao, private val app: Application, private val prefs: UserPreferences) : AndroidViewModel(app) {
    fun insert(user: Users) = viewModelScope.launch {
        userDao.insertUser(user)
    }

    suspend fun getUserByUsernameAndPassword(username: String, email: String, password: String): Users? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUsernameAndPassword(username, email, password)
        }
    }

    suspend fun checkIfUserExists(username: String, email: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.checkIfUserExists(username, email) > 0
        }
    }

    fun login() {
        viewModelScope.launch {
            if (!prefs.isUserAlreadyLoggedIn()) {
                prefs.saveUser()
            }
        }
    }

    suspend fun isUserAlreadyLoggedIn(): Boolean {
        return prefs.isLoggedIn().first()
    }

    fun clear() = viewModelScope.launch {
        prefs.clear()
    }
}