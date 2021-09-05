package com.example.mytasksapp.model

import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlinx.coroutines.launch

class CalendarViewModel(private val taskDao: TaskDao) : ViewModel() {



    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()


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
