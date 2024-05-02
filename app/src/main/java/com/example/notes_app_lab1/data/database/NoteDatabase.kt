package com.example.notes_app_lab1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes_app_lab1.data.dao.NoteDao
import com.example.notes_app_lab1.data.entity.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "NotesDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun noteDao(): NoteDao
}