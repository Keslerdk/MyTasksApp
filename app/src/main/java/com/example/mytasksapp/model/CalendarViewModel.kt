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

    //    val daysList = mutableListOf<String>()
    private val _dateList = mutableListOf<LocalDate>()
    val dateList: List<LocalDate> = _dateList

    private var _instanceDate = MutableLiveData<LocalDate>()
    val instanceDate: LiveData<LocalDate> = _instanceDate

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

    fun setDateList() {
        _dateList.add(instanceDate.value!!)
        for (i in 1..7) {
            _dateList.add(instanceDate.value!!.plusDays(i.toLong()))
        }
    }

    fun setSelectedDay (date: LocalDate) {
        _selectedDate.value = date
    }

    fun setRelevantTasks() {
        val tempList = mutableListOf<Task>()
        for (item in allTasks.value!!) {
            if (localDateToString(selectedDate.value).equals(item.date)) {
                tempList.add(item)
            }
        }
        _relevantTasks.value = tempList
    }

    fun localDateToString(date: LocalDate?): String {
        return date!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
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
