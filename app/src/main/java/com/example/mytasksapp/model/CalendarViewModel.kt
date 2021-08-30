package com.example.mytasksapp.model

import android.content.ClipData
import androidx.lifecycle.*
import androidx.room.ColumnInfo
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlinx.coroutines.launch

class CalendarViewModel(private val taskDao: TaskDao) : ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()
    lateinit var task: Task

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    private fun getNewtaskEntery(
        title: String,
        date: String,
        startTime: String,
        endTime: String,
        description: String,
        category: String
    ) : Task {
        return Task(
            title = title,
            date = date, startTime = startTime,
            endTime = endTime,
            description = description,
            category = category
        )
    }

    fun addNewItem(title: String,
                   date: String,
                   startTime: String,
                   endTime: String,
                   description: String,
                   category: String) {
        insertTask(getNewtaskEntery(title, date, startTime, endTime, description, category))
    }


}

class CalendarViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
