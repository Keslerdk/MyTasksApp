package com.example.mytasksapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mytasksapp.databinding.FragmentCreateNewTaskBinding
import com.example.mytasksapp.model.CalendarViewModel
import com.example.mytasksapp.model.CalendarViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class CreateNewTaskFragment : Fragment() {

    private var _binding: FragmentCreateNewTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory((activity?.application as MyTaskApplication).database.taskDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarFab.setOnClickListener {
            showDatePiker()
        }

        binding.editDateField.setOnKeyListener { v, keyCode, event -> handleKeyEvent(v, keyCode) }
        viewModel.date.observe(viewLifecycleOwner) {
            binding.editDateField.setText(it.toString())
        }

    }

    private fun showDatePiker() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val f = SimpleDateFormat("dd.MM.yyyy")

        Log.d(TAG, "showDatePiker: ${viewModel.date.value}")

        //Todo wrong parsing
        val d = f.parse(viewModel.date.value)
        val milliseconds = d.time


        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setTheme(R.style.Widget_MyTasksApp_MaterialDatePicker)
                //ToDO handle null exception
            .setSelection(milliseconds)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()


        datePicker.addOnPositiveButtonClickListener {
            getDatePickerResult(it)
        }

        datePicker.show(requireFragmentManager(), "tag")

    }
    /**
     * get date from calendar and formate it to string
     */
    private fun getDatePickerResult(date: Long) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.setTimeInMillis(date);

        val format = SimpleDateFormat("dd.MM.yyyy")
        val formatted: String = format.format(calendar.getTime())
        Log.d(TAG, "showDatePiker: ${formatted}")

        viewModel.setDate(formatted)

//        binding.editDateField.setText(formatted)
    }

    /**
     * when enter in clicked hide keybord and save date
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            //ToDo set errors
            viewModel.setDate(binding.editDateField.text.toString())
            return true
        }
        return false
    }

    companion object {
        private const val TAG = "CreateNewTaskFragment"
    }


}