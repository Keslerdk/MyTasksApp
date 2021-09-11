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
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewTaskBtn.setOnClickListener {
            navigateToNewTask()

        }

        val list = mutableListOf<String>()
        list.add("Mon")
        list.add("Tue")
        list.add("Wed")
        list.add("Thus")
        list.add("Fri")
        list.add("Sat")
        list.add("Sun")

//        viewModel.setDateList()
        binding.calendarWeekRecyclerView.adapter = CalendarDayAdapter(viewModel.dateList)
    }

    fun navigateToNewTask() {
        findNavController().navigate(R.id.action_calendarFragment_to_createNewTaskFragment)
    }

}