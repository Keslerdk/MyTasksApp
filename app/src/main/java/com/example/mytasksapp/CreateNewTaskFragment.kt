package com.example.mytasksapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.mytasksapp.databinding.FragmentCreateNewTaskBinding
import com.example.mytasksapp.model.NewTaskViewModel
import com.example.mytasksapp.model.NewTaskViewModelFactory
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


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
    ): View {
        _binding = FragmentCreateNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarFab.setOnClickListener {
            showDatePiker()
        }

        binding.editDateField.setOnKeyListener { v, keyCode, _ -> handleKeyEvent(v, keyCode) }

        /**
         * if text dont matches regex set error else save it
         */
        binding.editDateField.addTextChangedListener {
            if (!viewModel.isEntryDateValid(it.toString())) {
                binding.editDateLayout.isErrorEnabled = true
                binding.editDateLayout.error = "ДД.ММ.ГГГГ"
                //set button unclickable
                binding.createNewTaskBtn.isClickable = false
            } else {
                binding.editDateLayout.isErrorEnabled = false
                viewModel.stringToLocalDate(it.toString())
                binding.createNewTaskBtn.isClickable = true
            }
        }

        binding.editTitleField.addTextChangedListener {
            if (!viewModel.isEntryTitleValid(it.toString())) {
                binding.editTitleLayout.isErrorEnabled = true
                binding.editTitleLayout.error = "This field cant be empty!"
                //set button unclickable
                binding.createNewTaskBtn.isClickable = false
            } else {
                binding.editTitleLayout.isErrorEnabled = false
                binding.createNewTaskBtn.isClickable = true
            }
        }

        /**
         * call for start time picker
         */
        binding.startTimeField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showStartTimePicker()
            }
        }
        binding.startTimeField.setOnClickListener {
            showStartTimePicker()
        }

        /**
         * call for end time picker
         */
        binding.endTimeField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showEndTimePicker()
            }
        }
        binding.endTimeField.setOnClickListener {
            showEndTimePicker()
        }

        /**
         * add new task to database and navigate up
         */
        binding.createNewTaskBtn.setOnClickListener {
            viewModel.addNewItem(
                binding.editTitleField.text.toString(),
                viewModel.localDateToString(),
                binding.startTimeField.text.toString(),
                binding.endTimeField.text.toString(),
                binding.descriptionField.text.toString(),
                "Sport App"
            )
            view.findNavController().navigateUp()
        }

//        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->  }
//        binding.chipGroup.

        val selectedChipId = binding.chipGroup.checkedChipId
        binding.bankingAppChip.setChipBackgroundColorResource(R.color.pink)
//        val selectedChip = view.findViewById<View>(selectedChipId)
//        selectedChip.setChipBackgroundColorResource(R.color.pink)
//        selectedChip.setBackgroundColor(resources.getColor(R.color.pink))

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
        } catch (e: Exception) {
        }

        val datePicker = builder.build()

        //set result in view model
        datePicker.addOnPositiveButtonClickListener {
            // convert date and set it to edit text
            viewModel.fromMilliSecondsToLocalDate(it)
            binding.editDateField.setText(viewModel.localDateToString())
        }

        datePicker.show(requireFragmentManager(), "tag")

    }

    private fun buildStartTimePicker(hours: Int, minutes: Int) {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hours)
                .setMinute(minutes)
                .setTitleText("Select start time")
                .build()

        picker.addOnPositiveButtonClickListener {
            setStartTime(picker.hour, picker.minute)
        }

        picker.show(requireFragmentManager(), "tag")
    }

    private fun buildEndTimePicker(hours: Int, minutes: Int) {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hours)
                .setMinute(minutes)
                .setTitleText("Select end time")
                .build()

        picker.addOnPositiveButtonClickListener {
            setEndTime(picker.hour, picker.minute)
        }

        picker.show(requireFragmentManager(), "tag")
    }


    /**
     * show time pickers
     * if edit filed is not empty set data to clock from there
     */
    private fun showStartTimePicker() {
        if (!binding.startTimeField.text.isNullOrEmpty()) {
            buildStartTimePicker(
                binding.startTimeField.text.toString().split(":")[0].toInt(),
                binding.startTimeField.text.toString().split(":")[1].toInt()
            )
        } else buildStartTimePicker(12, 0)
    }

    private fun showEndTimePicker() {
        if (!binding.endTimeField.text.isNullOrEmpty()) {
            buildEndTimePicker(
                binding.endTimeField.text.toString().split(":")[0].toInt(),
                binding.endTimeField.text.toString().split(":")[1].toInt()
            )
        } else buildEndTimePicker(12, 0)
    }

    /**
     * setting time picker result to fields
     */
    @SuppressLint("SetTextI18n")
    private fun setStartTime(hours: Int, minutes: Int) {
        binding.startTimeField.setText(
            //format string to time
            "${String.format("%02d", hours)}:${
                String.format(
                    "%02d",
                    minutes
                )
            }"
        )

    }

    @SuppressLint("SetTextI18n")
    private fun setEndTime(hours: Int, minutes: Int) {
        binding.endTimeField.setText(
            "${String.format("%02d", hours)}:${
                String.format(
                    "%02d",
                    minutes
                )
            }"
        )
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