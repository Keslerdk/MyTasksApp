package com.example.mytasksapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.mytasksapp.databinding.ActiveProjectsItemBinding
import com.example.mytasksapp.databinding.FragmentMainBinding
import com.example.mytasksapp.model.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((activity?.application as MyTaskApplication).database.taskDao())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainFragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //list of app names
        val nameList = listOf("Sport App", "Medical App", "Rent App", "Banking App")
        binding.activeProjectsRecyclerView.adapter =
            ActiveProjectsAdapter(nameList, viewModel, viewLifecycleOwner)

        // when all tasks change update status and percents
        viewModel.allTasks.observe(viewLifecycleOwner) {
            viewModel.countStatus()
            viewModel.setPer()
        }

    }

    fun navigateToCalendar() {
        findNavController().navigate(R.id.action_mainFragment_to_calendarFragment)
    }

    companion object {
        private const val TAG = "MainFragment"
    }


}

class MyProgressFormatter : CircleProgressBar.ProgressFormatter {
    override fun format(p0: Int, p1: Int): CharSequence {
        return String.format("%d%%", (p0.toFloat() / p1.toFloat() * 100).toInt())
    }

}


