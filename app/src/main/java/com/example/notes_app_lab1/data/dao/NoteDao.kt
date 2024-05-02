package com.example.notes_app_lab1.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notes_app_lab1.data.entity.Note

@Dao
interface NoteDao {
    @Query("SELECT  * FROM notes ORDER BY date")
    fun getAllNotesSortedByDate(): List<Note>

    @Query("SELECT  * FROM notes ORDER BY labels")
    fun getAllNotesSortedByLabels(): List<Note>

    @Query("SELECT  * FROM notes WHERE instr(labels, :searchingLabel) > 0")
    fun getAllNotesFoundedByLabel(searchingLabel: String): List<Note>

    @Upsert
    suspend fun insetNote(note: Note)

    @Delete
    suspend fun delete(note: Note)


}