package com.example.mytasksapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.databinding.TimeTableItemBinding

class TimeTableAdapter : ListAdapter<Task, TimeTableAdapter.TimeTableViewHolder>(Diffcallback) {

    class TimeTableViewHolder(private var binding: TimeTableItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.titleProject.text = task.title
//            binding.description.text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
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
    }

    object Diffcallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.equals(newItem)
        }

    }
}