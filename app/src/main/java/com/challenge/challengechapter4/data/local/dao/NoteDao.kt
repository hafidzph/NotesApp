package com.challenge.challengechapter4.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.challenge.challengechapter4.data.local.data.Notes

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Notes)

    @Query("SELECT * FROM notes WHERE userId = :userId")
    fun getAllNotes(userId: Int): LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY title ASC")
    fun getAllNotesAsc(userId: Int): LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY title DESC")
    fun getAllNotesDesc(userId: Int): LiveData<List<Notes>>

    @Query("UPDATE notes SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: Int, title: String): Int

    @Query("UPDATE notes SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String): Int

    @Query("UPDATE notes SET title = :title, note = :note WHERE id = :id")
    suspend fun update(id: Int, title: String, note: String): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int): Int
}