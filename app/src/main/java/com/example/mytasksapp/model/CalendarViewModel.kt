package com.example.mytasksapp.model

import android.util.Log
import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class CalendarViewModel(private val taskDao: TaskDao) : ViewModel() {

    // list of dates to calendar recycler view
    private val _dateList = mutableListOf<LocalDate>()
    val dateList: List<LocalDate> = _dateList

    private var _instanceDate = MutableLiveData<LocalDate>()
    val instanceDate: LiveData<LocalDate> = _instanceDate

    // all tasks from database
    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    private var _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private var _relevantTasks = MutableLiveData<List<Task>>()
    val relevantTasks: LiveData<List<Task>> = _relevantTasks

    init {
        _instanceDate.value = LocalDate.now(ZoneId.of("UTC"))
        setDateList()
        Log.d(TAG, "init: ${allTasks.value}")

        _selectedDate.value = instanceDate.value

    }

    /**
     * set next week dates in list
     */
    private fun setDateList() {
        _dateList.add(instanceDate.value!!)
        for (i in 1..7) {
            _dateList.add(instanceDate.value!!.plusDays(i.toLong()))
        }
    }

    /**
     * function to update selected day from CalendarDayAdapter
     */
    fun setSelectedDay(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * filter all tasks to selected date
     */
    fun setRelevantTasks() {
        // temporary list
        val tempList = mutableListOf<Task>()

        if (!allTasks.value.isNullOrEmpty()) {
            for (item in allTasks.value!!) {
                //if date in task is equals to selected date add to relevant list
                if (localDateToString(selectedDate.value).equals(item.date)) {
                    tempList.add(item)
                }
            }
            tempList.sortBy { task: Task ->  task.startTime}
            _relevantTasks.value = tempList
        }
    }

    /**
     *parsing local date format
     */
    private fun localDateToString(date: LocalDate?): String {
        return date!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    fun stringToLocalDate(text: String): LocalDate {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    /**
     * delete selected items
     */
    fun deleteTask(task: Task) {
       viewModelScope.launch {
           taskDao.delete(task)
       }
    }

    private fun updateTask(status: String, id: Int) {
        viewModelScope.launch {
            taskDao.update(status, id)
        }
    }

    fun updateItem(status: String, id: Int) {
        updateTask(status, id)
    }
    companion object {
        private const val TAG = "CalendarViewModel"
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
