package com.example.mytasksapp.model

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.data.TaskDao
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class NewTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    private var _date = MutableLiveData<LocalDate>()
    val date: LiveData<LocalDate> = _date

    lateinit var task: Task


    fun localDateToString(): String {
        return date.value!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    fun stringToLocalDate(text: String) {
        _date.value = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    /**
     * get date from calendar and formate it to local date
     */
    @SuppressLint("SimpleDateFormat")
    fun fromMilliSecondsToLocalDate(date: Long) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = date
        val localDate = LocalDate.parse(SimpleDateFormat("yyyy-MM-dd").format(date))
        _date.value = localDate

    }

    /**
     * converting local date to milliseconds to set it on calendar
     */
    fun fromLocalDateToMilliSeconds(): Long {
        return LocalDateTime.of(date.value, LocalTime.of(0, 0))
            .atZone(ZoneId.of("UTC"))
            .toInstant().toEpochMilli()
    }

    fun isEntryDateValid(text: String): Boolean {
        //regex for dd.MM.yyyy
        val regex =
            Regex(pattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")
        //  checking the length of the year
        var correctLength = false
        if (text.split(".").last().length == 4) correctLength = true
        return regex.containsMatchIn(text) and correctLength
    }

    fun isEntryTitleValid(text: String?): Boolean {
        return !text.isNullOrEmpty()
    }

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

class NewTaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewTaskViewModel(taskDao = taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}