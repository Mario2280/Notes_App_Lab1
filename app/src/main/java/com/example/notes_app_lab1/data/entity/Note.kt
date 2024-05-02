package com.example.notes_app_lab1.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val labels: String?,
    val content: String?,
    val date: Long,
)