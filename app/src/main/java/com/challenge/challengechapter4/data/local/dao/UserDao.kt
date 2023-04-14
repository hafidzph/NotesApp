package com.challenge.challengechapter4.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.challenge.challengechapter4.data.local.data.Users

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM users WHERE (username = :username OR email = :email) AND password = :password")
    suspend fun getUserByUsernameAndPassword(username: String, email: String, password: String): Users?

    @Query("SELECT COUNT(*) FROM users WHERE username = :username OR email = :email")
    suspend fun checkIfUserExists(username: String, email: String): Int
}
