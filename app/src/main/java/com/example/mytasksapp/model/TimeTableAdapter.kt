package com.example.mytasksapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.R
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.databinding.TimeTableItemBinding

class TimeTableAdapter() :
    ListAdapter<Task, TimeTableAdapter.TimeTableViewHolder>(
        Diffcallback
    ) {


    class TimeTableViewHolder(
        private var binding: TimeTableItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {

                binding.titleProject.text = task.title

                if (task.description.isNullOrEmpty()) binding.description.visibility = View.GONE
                else binding.description.text = task.description

                if (!task.startTime.isNullOrEmpty() and !task.endTime.isNullOrEmpty()) {
                    binding.startTime.text = task.startTime
                    binding.endTime.text = task.endTime
                } else {
                    binding.startTime.visibility = View.GONE;
                    binding.endTime.visibility = View.GONE;
                }

                when (task.category) {
                    "Sport App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_blue)
                    "Medical App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_pink)
                    "Rent App" -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_green)
                    else -> binding.taskLayout.setBackgroundResource(R.drawable.task_background_yellow)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableAdapter.TimeTableViewHolder {
        return TimeTableAdapter.TimeTableViewHolder(
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
    }

    object Diffcallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.equals(newItem)
        }

    }
}