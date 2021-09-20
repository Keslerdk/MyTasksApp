package com.example.mytasksapp

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
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

    var myActMode: ActionMode? = null
    var adapter: TimeTableAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.calendarFragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set adapters to recycler views
        binding.calendarWeekRecyclerView.adapter = CalendarDayAdapter(viewModel.dateList, viewModel
        ) {
            // finish action mode when another day choose
            ActionModeCallback().onDestroyActionMode(myActMode)
        }

        // adapter for recycler view with tasks
        adapter = TimeTableAdapter()
        // set adapter
        binding.timeTableRecyclerView.adapter = adapter

        // set click actions
        adapter!!.onItemClicked = {
            if (myActMode != null) {
                toggleSelection(it, adapter!!)
            }
        }
        adapter!!.onLongClicked = {
            if (myActMode == null) {
                myActMode = binding.topAppBar.startActionMode(ActionModeCallback())
            }

            toggleSelection(it, adapter!!)
        }

        // when tasks in  database change update relevant tasks
        viewModel.allTasks.observe(viewLifecycleOwner) {
            viewModel.setRelevantTasks()
        }

        //when selected date changes update relevant tasks
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            viewModel.setRelevantTasks()
        }

//        binding.date.isLongClickable = true
//        binding.date.setOnLongClickListener {
//            if (myActMode != null) {
//                return@setOnLongClickListener false;
//            }
////            myActMode = activity?.startActionMode(ActionModeCallback());
//            binding.topAppBar.startActionMode(ActionModeCallback())
//            return@setOnLongClickListener true
//        }

        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(binding.topAppBar, navHostFragment)
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

    /**
     * modified toggle from adapter for contextual action bar
     */
    private fun toggleSelection(position: Int, adapter: TimeTableAdapter) {
        adapter.toggleSelection(position)
        val count: Int = adapter.getSelectedItemCount()
        if (count == 0) {
            myActMode?.finish()
        } else {
            myActMode?.title = (resources.getString(R.string.action_mode_title,  count) )
            myActMode?.invalidate()
        }
    }

    /**
     * action mode for contextual action bar
     */
    inner class ActionModeCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu, menu)
            mode?.title = "Select Options"
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT)
                    .show()
                else -> return true
            }
            return false
        }


        override fun onDestroyActionMode(mode: ActionMode?) {
            mode?.finish()
            adapter!!.clearSelection()
            myActMode = null
        }

    }

}

