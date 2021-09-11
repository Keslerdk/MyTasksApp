package com.example.mytasksapp.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.R
import com.example.mytasksapp.databinding.CalendarDayBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarDayAdapter(private val list: List<LocalDate>) :
    RecyclerView.Adapter<CalendarDayAdapter.CalendarDayViewHolder>() {

    var selectedItemId = 0
    var lastSelectedId = 0

    class CalendarDayViewHolder(private val binding: CalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(localDate: LocalDate) {
            binding.day.text = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
            binding.date.text = localDate.dayOfMonth.toString()

        }

        fun setColor(colorId: Int) {
            binding.day.setTextColor(ContextCompat.getColor(binding.date.context, colorId))
            binding.date.setTextColor(ContextCompat.getColor(binding.date.context, colorId))
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

        val textColor: Int = if (position==selectedItemId) R.color.pink else R.color.oxford_blue
        holder.setColor(textColor)

        holder.itemView.setOnClickListener {
            lastSelectedId = selectedItemId
            selectedItemId = position

            notifyItemChanged(lastSelectedId)
            notifyItemChanged(selectedItemId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}