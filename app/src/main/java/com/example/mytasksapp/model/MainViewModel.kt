package com.example.mytasksapp.model

import android.util.Log
import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlin.math.log

class MainViewModel(private val taskDao: TaskDao) : ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    private var _sportAppPer = MutableLiveData<Int>(0)
    val sportAppPer: LiveData<Int> = _sportAppPer

    private var _medicalAppPer = MutableLiveData<Int>(0)
    val medicalAppPer: LiveData<Int> = _medicalAppPer

    private var _rentAppPer = MutableLiveData<Int>(0)
    val rentApp: LiveData<Int> = _rentAppPer

    private var _bankingAppPer = MutableLiveData<Int>(0)
    val bankingAppPer: LiveData<Int> = _bankingAppPer

    private var _toDoNum = MutableLiveData<Int>(0)
    val toDoNum: LiveData<Int> = _toDoNum

    private var _inProcessNum = MutableLiveData<Int>(0)
    val inProcessNum: LiveData<Int> = _inProcessNum

    private var _doneNum = MutableLiveData<Int>(0)
    val doneNum: LiveData<Int> = _doneNum

    fun countStatus() {
        if (!allTasks.value.isNullOrEmpty()) {
            var toDo = 0
            var inProcess = 0
            var done = 0
            allTasks.value!!.forEach {
                when (it.status) {
                    "To Do" ->
                        toDo += 1
                    "In process" -> inProcess+=1
                    "Done" -> done+=1
                }
            }
            _toDoNum.value = toDo
            _inProcessNum.value = inProcess
            _doneNum.value = done
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

class MainViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
