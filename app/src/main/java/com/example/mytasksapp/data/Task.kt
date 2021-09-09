package com.example.mytasksapp.data

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "task")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NotNull
    @ColumnInfo(name = "title")
    val title: String,
    @NotNull
    @ColumnInfo(name = "date")
    val date: String,
    @Nullable
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @Nullable
    @ColumnInfo(name = "end_time")
    val endTime: String,
    @Nullable
    @ColumnInfo(name = "description")
    val description: String,
    @NotNull
    @ColumnInfo(name = "category")
    val category: String
)