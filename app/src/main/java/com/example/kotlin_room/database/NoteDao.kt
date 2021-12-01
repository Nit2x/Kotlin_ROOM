package com.example.kotlin_room.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("UPDATE note SET judul=:Judul, deskripsi=:deskripsi WHERE id=:Noid")
    suspend fun update(Judul:String, deskripsi:String, Noid: Int)

    @Delete
    suspend fun delete(note : Note)

    @Query("SELECT * from note ORDER BY id ASC")
    suspend fun selectall() : MutableList<Note>

    @Query("SELECT * from note WHERE id=:Noid")
    suspend fun getNote(Noid:Int) : Note
}