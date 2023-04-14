package com.challenge.challengechapter4.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.challenge.challengechapter4.data.local.dao.NoteDao
import com.challenge.challengechapter4.data.local.dao.UserDao
import com.challenge.challengechapter4.data.local.data.Notes
import com.challenge.challengechapter4.data.local.data.Users
import kotlinx.coroutines.runBlocking

@Database(entities = [Users::class, Notes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { instance = it }
            }
        }
    }
}
