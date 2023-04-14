package com.challenge.challengechapter4.data.ui.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.challenge.challengechapter4.data.local.data.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserPreferences(private val context: Context) {
    companion object {
        private val Context.counterDataStore by preferencesDataStore(
            name = "user_prefs"
        )
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveUser() {
        context.counterDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clear() {
        context.counterDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isLoggedIn(): Flow<Boolean>{
        return context.counterDataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    fun isUserAlreadyLoggedIn(): Boolean {
        return runBlocking {
            isLoggedIn().first()
        }
    }
}
