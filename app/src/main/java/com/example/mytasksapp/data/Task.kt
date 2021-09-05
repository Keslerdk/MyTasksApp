package com.example.mytasksapp.data

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @Nullable
    @ColumnInfo(name = "end_time")
    val endTime: String,
    @Nullable
    @ColumnInfo(name = "description")
    val description: String,
    @Nullable
    @ColumnInfo(name = "category")
    val category: String
)