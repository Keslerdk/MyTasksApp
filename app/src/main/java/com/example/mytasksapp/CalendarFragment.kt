package com.example.mytasksapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mytasksapp.databinding.FragmentCalendarBinding
import com.example.mytasksapp.model.CalendarDayAdapter
import com.example.mytasksapp.model.CalendarViewModel
import com.example.mytasksapp.model.CalendarViewModelFactory
import com.example.mytasksapp.model.TimeTableAdapter

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory(
            (activity?.application as MyTaskApplication).database
                .taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner= this
        binding.calendarFragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set adapters to recycler views
        binding.calendarWeekRecyclerView.adapter = CalendarDayAdapter(viewModel.dateList, viewModel)
        binding.timeTableRecyclerView.adapter = TimeTableAdapter()

        // when tasks in  database change update relevant tasks
        viewModel.allTasks.observe(viewLifecycleOwner) {
            viewModel.setRelevantTasks()
        }

        //when selected date changes update relevant tasks
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            viewModel.setRelevantTasks()
        }
    }

    /**
     * navigate to next fragment
     */
    fun navigateToNewTask() {
        findNavController().navigate(R.id.action_calendarFragment_to_createNewTaskFragment)
    }

    companion object {
        private const val TAG = "CalendarFragment"
    }

}