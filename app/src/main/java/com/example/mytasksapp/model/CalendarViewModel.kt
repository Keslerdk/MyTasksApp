package com.example.mytasksapp.model

import android.util.Log
import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

class CalendarViewModel(private val taskDao: TaskDao) : ViewModel() {

//    val daysList = mutableListOf<String>()
    private val _dateList = mutableListOf<LocalDate>()
    val dateList: List<LocalDate> = _dateList

    private var _instanceDate = MutableLiveData<LocalDate>()
    val instanceDate: LiveData<LocalDate> = _instanceDate

    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    init {
        _instanceDate.value = LocalDate.now(ZoneId.of("UTC"))
//        Log.d(Companion.TAG, "init: ${instanceDate.value?.month}")
        setDateList()
    }

    fun setDateList() {
        _dateList.add(instanceDate.value!!)
        for (i in 1..7) {
            _dateList.add(instanceDate.value!!.plusDays(i.toLong()))
            Log.d(TAG, "setDateList: ${dateList.get(i).dayOfMonth}")
        }
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
