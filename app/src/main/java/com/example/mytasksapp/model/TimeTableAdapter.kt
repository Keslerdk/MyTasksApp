package com.example.mytasksapp.model

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.R
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.databinding.TimeTableItemBinding

class TimeTableAdapter(
    var onItemClicked: ((position: Int) -> Unit)? = null,
    var onLongClicked: ((Int) -> Unit)? = null
) :
    ListAdapter<Task, TimeTableAdapter.TimeTableViewHolder>(
        Diffcallback
    ) {

    val selectedItems = SparseBooleanArray()

    class TimeTableViewHolder(
        private var binding: TimeTableItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {

            binding.titleProject.text = task.title

            if (task.description.isEmpty()) binding.description.visibility = View.GONE
            else binding.description.text = task.description

            if (task.startTime.isNotEmpty() and task.endTime.isNotEmpty()) {
                binding.startTime.text = task.startTime
                binding.endTime.text = task.endTime
            } else {
                binding.startTime.visibility = View.GONE
                binding.endTime.visibility = View.GONE
            }

            when (task.category) {
                "Sport App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_blue)
                "Medical App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_pink)
                "Rent App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_green)
                else -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_yellow)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeTableViewHolder {
        return TimeTableViewHolder(
            TimeTableItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(position)
        }
        holder.itemView.isActivated = selectedItems.get(position)

        holder.itemView.setOnLongClickListener {

            onLongClicked?.invoke(position)
            return@setOnLongClickListener true
        }
    }

    object Diffcallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }

//    /**
//     * Indicates if the item at position position is selected
//     * @param position Position of the item to check
//     * @return true if the item is selected, false otherwise
//     */
//    fun isSelected(position: Int): Boolean {
//        return getSelectedItems()!!.contains(position)
//    }

    fun getSelectedItemCount(): Int {
        return selectedItems.size()
    }

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    private fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    fun toggleSelection(position: Int) {
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
//            Log.d(TAG, "toggleSelection: unselected")
        } else {
            selectedItems.put(position, true)
//            Log.d(TAG, "toggleSelection: selected")
        }
        notifyItemChanged(position)
    }

    /**
     * Clear the selection status for all items
     */
    fun clearSelection() {
        val selection = getSelectedItems()
        selectedItems.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    private val TAG = "TimeTableAdapter"
}