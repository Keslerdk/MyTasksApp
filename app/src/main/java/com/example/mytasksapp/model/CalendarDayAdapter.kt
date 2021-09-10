package com.example.mytasksapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.databinding.CalendarDayBinding

class CalendarDayAdapter(private val list: List<String>) : RecyclerView.Adapter<CalendarDayAdapter.CalendarDayViewHolder>() {

    class CalendarDayViewHolder(private val binding: CalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: String) {
            binding.day.text = day
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        return CalendarDayViewHolder(
            CalendarDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalendarDayViewHolder, position: Int) {
        val currentItem = list.get(position)

        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}