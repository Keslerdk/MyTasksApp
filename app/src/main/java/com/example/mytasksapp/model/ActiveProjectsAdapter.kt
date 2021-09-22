package com.example.mytasksapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.MyProgressFormatter
import com.example.mytasksapp.R
import com.example.mytasksapp.databinding.ActiveProjectsItemBinding

class ActiveProjectsAdapter(private val nameList: List<String>) :
    RecyclerView.Adapter<ActiveProjectsAdapter.ActiveProjectsViewHolder>() {

    class ActiveProjectsViewHolder(private val binding: ActiveProjectsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) {
            binding.lineProgress.setProgressFormatter(MyProgressFormatter())
            binding.lineProgress.progress = 25

            when (name) {
                binding.lineProgress.resources.getString(R.string.sport_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.sport_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_green)
                }
                binding.lineProgress.resources.getString(R.string.medical_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.medical_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_pink)
                }
                binding.lineProgress.resources.getString(R.string.rent_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.rent_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_blue)
                }
                binding.lineProgress.resources.getString(R.string.banking_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.banking_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_yellow)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveProjectsViewHolder {
        return ActiveProjectsViewHolder(
            ActiveProjectsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActiveProjectsViewHolder, position: Int) {
        val currentItem = nameList[position]

        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }
}