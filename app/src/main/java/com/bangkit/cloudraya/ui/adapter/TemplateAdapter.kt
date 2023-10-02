package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemServerBinding
import com.bangkit.cloudraya.model.remote.TemplatesItem

class TemplateAdapter: RecyclerView.Adapter<TemplateAdapter.MyViewHolder>() {
    private var data: List<TemplatesItem> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemServerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)

    }

    fun submitData(newData: List<TemplatesItem>) {
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

    inner class MyViewHolder(private val binding: ItemServerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TemplatesItem) {
            binding.serverName.text = data.name
            binding.hardDisk.text = data.sizeGb.toString()
            binding.tvCost.text = data.prices?.get(0)?.priceOs.toString()
            binding.tvDesc.text = data.description
        }
    }
}