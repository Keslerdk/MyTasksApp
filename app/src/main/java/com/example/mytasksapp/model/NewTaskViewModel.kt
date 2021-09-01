package com.example.mytasksapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasksapp.data.TaskDao
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class NewTaskViewModel(private val taskDao: TaskDao): ViewModel() {

    private var _date = MutableLiveData<LocalDate>()
    val date : LiveData<LocalDate> = _date

    fun localDateToString() : String {
        return date.value!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    fun stringToLocalDate(text: String) {
        _date.value = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    /**
     * get date from calendar and formate it to local date
     */
    fun fromMilliSecondsToLocalDate(date: Long) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.setTimeInMillis(date);
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
}

class NewTaskViewModelFactory(private val taskDao: TaskDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewTaskViewModel(taskDao = taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}