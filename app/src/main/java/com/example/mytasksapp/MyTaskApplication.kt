package com.example.mytasksapp

import android.app.Application
import com.example.mytasksapp.data.TaskRoomDatabase

class MyTaskApplication: Application() {
    val database: TaskRoomDatabase by lazy {
        TaskRoomDatabase.getDatabase(this)
    }
}