package com.example.mytasksapp.model

import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao

class MainViewModel(taskDao: TaskDao) : ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    //percents of done tasks
    private var _sportAppPer = MutableLiveData(0)
    val sportAppPer: LiveData<Int> = _sportAppPer

    private var _medicalAppPer = MutableLiveData(0)
    val medicalAppPer: LiveData<Int> = _medicalAppPer

    private var _rentAppPer = MutableLiveData(0)
    val rentAppPer: LiveData<Int> = _rentAppPer

    private var _bankingAppPer = MutableLiveData(0)
    val bankingAppPer: LiveData<Int> = _bankingAppPer

    //number of tasks in different status
    private var _toDoNum = MutableLiveData(0)
    val toDoNum: LiveData<Int> = _toDoNum

    private var _inProcessNum = MutableLiveData(0)
    val inProcessNum: LiveData<Int> = _inProcessNum

    private var _doneNum = MutableLiveData(0)
    val doneNum: LiveData<Int> = _doneNum

    /**
     *set new number
     */
    fun countStatus() {
        if (!allTasks.value.isNullOrEmpty()) {
            //temple vars
            var toDo = 0
            var inProcess = 0
            var done = 0
            allTasks.value!!.forEach {
                when (it.status) {
                    "To Do" ->
                        toDo += 1
                    "In process" -> inProcess += 1
                    "Done" -> done += 1
                }
            }
            _toDoNum.value = toDo
            _inProcessNum.value = inProcess
            _doneNum.value = done
        }
    }

    /**
     * set new percents
     */
    fun setPer() {
        if (!allTasks.value.isNullOrEmpty()) {
            //auxiliary vars to count percents
            var numSportApp = 0
            var doneSportApp = 0
            var numMedicalApp = 0
            var doneMedicalApp = 0
            var numRentApp = 0
            var doneRentApp = 0
            var numBankingApp = 0
            var doneBankingApp = 0
            allTasks.value!!.forEach {
                when (it.category) {
                    "Sport App" -> {
                        numSportApp += 1
                        if (it.status == "Done") doneSportApp += 1
                    }
                    "Medical App" -> {
                        numMedicalApp += 1
                        if (it.status == "Done") doneMedicalApp += 1
                    }
                    "Rent App" -> {
                        numRentApp += 1
                        if (it.status == "Done") doneRentApp += 1
                    }
                    "Banking App" -> {
                        numBankingApp += 1
                        if (it.status == "Done") doneBankingApp += 1
                    }
                }
            }

            _sportAppPer.value =
                if (numSportApp != 0) ((doneSportApp.toFloat() / numSportApp) * 100).toInt() else 0
            _medicalAppPer.value =
                if (numMedicalApp != 0) ((doneMedicalApp.toFloat() / numMedicalApp) * 100).toInt() else 0
            _rentAppPer.value =
                if (numRentApp != 0) ((doneRentApp.toFloat() / numRentApp) * 100).toInt() else 0
            _bankingAppPer.value =
                if (numBankingApp != 0) ((doneBankingApp.toFloat() / numBankingApp) * 100).toInt() else 0
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
