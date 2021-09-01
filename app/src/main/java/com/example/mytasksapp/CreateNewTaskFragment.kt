package com.example.mytasksapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mytasksapp.databinding.FragmentCreateNewTaskBinding
import com.example.mytasksapp.model.NewTaskViewModel
import com.example.mytasksapp.model.NewTaskViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.*


class CreateNewTaskFragment : Fragment() {

    private var _binding: FragmentCreateNewTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewTaskViewModel by activityViewModels {
        NewTaskViewModelFactory((activity?.application as MyTaskApplication).database.taskDao())
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

        /**
         * if text dont matches regex set error else save it
         */
        binding.editDateField.addTextChangedListener {
            if (!viewModel.isEntryDateValid(it.toString())) {
                binding.editDateLayout.isErrorEnabled = true
                binding.editDateLayout.error = "ДД.ММ.ГГГГ"
            } else {
                binding.editDateLayout.isErrorEnabled = false
                viewModel.stringToLocalDate(it.toString())
            }
        }

//        viewModel.date.observe(viewLifecycleOwner) {
//            binding.editDateField.setText(viewModel.localDateToString())
//        }

    }

    private fun showDatePiker() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setTheme(R.style.Widget_MyTasksApp_MaterialDatePicker)
            .setCalendarConstraints(constraintsBuilder.build())

        // if viewModel.date is not empty set selection
        try {
            builder.setSelection(viewModel.fromLocalDateToMilliSeconds())
        } catch (e: Exception) {}

        val datePicker = builder.build()


        //set result in view model
        datePicker.addOnPositiveButtonClickListener {
            // convert date and set it to edit text
            viewModel.fromMilliSecondsToLocalDate(it)
            binding.editDateField.setText(viewModel.localDateToString())
        }

        datePicker.show(requireFragmentManager(), "tag")

    }

    /**
     * when enter in clicked hide keybord
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    companion object {
        private const val TAG = "CreateNewTaskFragment"
    }

}