package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemPackageBinding
import com.bangkit.cloudraya.model.remote.PackageItem

class PackageAdapter: RecyclerView.Adapter<PackageAdapter.MyViewHolder>() {
    private var data: List<PackageItem> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)

    }

    fun submitData(newData: List<PackageItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = data.size

            override fun getNewListSize() = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].id == newData[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        })
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: ItemPackageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PackageItem) {
            binding.tvPackage.text = data.name
            binding.tvMemory.text = data.memory.toString()
            binding.tvCpu.text = data.cpucore.toString()
            binding.tvBandwidth.text = data.bandwidth.toString()
            binding.tvDisk.text = data.rootDiskSize
            binding.tvHour.text = String.format("%.2f", data.priceOn[0].price)
            binding.tvMonthly.text = String.format("%.2f", (data.priceOn[0].price * 720))
        }
    }
}