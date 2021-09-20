package com.example.mytasksapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao


    companion object {
        @Volatile
        var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context): TaskRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
