package com.example.mytasksapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.util.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mytasksapp.data.Task
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
        binding.calendarWeekRecyclerView.adapter = CalendarDayAdapter(
            viewModel.dateList, viewModel
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
            Log.d(TAG, "onViewCreated: ${viewModel.relevantTasks.value}")
        }

        //when selected date changes update relevant tasks
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            viewModel.setRelevantTasks()
        }

        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(binding.topAppBar, navHostFragment)
    }

    /**
     * navigate to next fragment
     */
    fun navigateToNewTask() {
        ActionModeCallback().onDestroyActionMode(myActMode)
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
            myActMode?.title = (resources.getString(R.string.action_mode_title, count))
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
                R.id.action_delete -> {
                    forEach {
                        viewModel.deleteTask(it)
                    }
                }
                R.id.action_in_process -> {
                    forEach {
                        viewModel.updateItem("In process", it.id)
                    }
                }
                R.id.action_to_do -> {
                    forEach {
                        viewModel.updateItem("To Do", it.id)
                    }
                }
                R.id.action_done -> {
                    forEach {
                        viewModel.updateItem("Done", it.id)
                    }
                }
                else -> return true
            }
            return false
        }

        private fun forEach(action: (Task) -> Unit) {
            val selectedItems = adapter?.selectedItems
//            Log.d(TAG, "forEach: ${selectedItems}")
            selectedItems?.forEach { item, flag ->
                if (flag) {
//                    Log.d(TAG, "forEach: ${viewModel.relevantTasks.value?.get(item)!!}")
                    action(viewModel.relevantTasks.value?.get(item)!!)

                }
            }
            onDestroyActionMode(myActMode)
        }


        override fun onDestroyActionMode(mode: ActionMode?) {
            mode?.finish()
            adapter!!.clearSelection()
            myActMode = null
        }

    }

}

