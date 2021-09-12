package com.example.mytasksapp

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.data.Task
import com.example.mytasksapp.model.TimeTableAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: LiveData<List<Task>> ?) {
    val adapter = recyclerView.adapter as TimeTableAdapter
    adapter.submitList(data?.value)
}