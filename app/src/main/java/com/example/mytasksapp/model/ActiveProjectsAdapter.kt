package com.example.mytasksapp.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksapp.MyProgressFormatter
import com.example.mytasksapp.R
import com.example.mytasksapp.databinding.ActiveProjectsItemBinding

class ActiveProjectsAdapter(
    private val nameList: List<String>,
    private val viewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<ActiveProjectsAdapter.ActiveProjectsViewHolder>() {

    class ActiveProjectsViewHolder(
        private val binding: ActiveProjectsItemBinding,
        private val viewModel: MainViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) {
            binding.lineProgress.setProgressFormatter(MyProgressFormatter())

            when (name) {
                binding.lineProgress.resources.getString(R.string.sport_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.sport_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_green)
                    viewModel.sportAppPer.observe(binding.lifecycleOwner!!) {
                        binding.lineProgress.progress = it
                    }

                }
                binding.lineProgress.resources.getString(R.string.medical_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.medical_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_pink)
                    viewModel.medicalAppPer.observe(binding.lifecycleOwner!!) {
                        binding.lineProgress.progress = it
                    }
                }
                binding.lineProgress.resources.getString(R.string.rent_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.rent_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_blue)
                    viewModel.rentAppPer.observe(binding.lifecycleOwner!!) {
                        binding.lineProgress.progress = it
                    }
                }
                binding.lineProgress.resources.getString(R.string.banking_app) -> {
                    binding.name.text =
                        binding.lineProgress.resources.getString(R.string.banking_app)
                    binding.item.setBackgroundResource(R.drawable.active_projects_background_yellow)
                    viewModel.bankingAppPer.observe(binding.lifecycleOwner!!) {
                        binding.lineProgress.progress = it
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveProjectsViewHolder {
        val binding = ActiveProjectsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        binding.lifecycleOwner = lifecycleOwner
        return ActiveProjectsViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ActiveProjectsViewHolder, position: Int) {
        val currentItem = nameList[position]

        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }
}