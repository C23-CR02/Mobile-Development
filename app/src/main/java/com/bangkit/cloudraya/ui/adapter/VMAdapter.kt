package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemVmBinding
import com.bangkit.cloudraya.model.remote.ServersItem

class VMAdapter(private val onItemClick: (ServersItem) -> Unit) :
    RecyclerView.Adapter<VMAdapter.HolderVM>() {
    private var data: List<ServersItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderVM {
        return HolderVM(ItemVmBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HolderVM, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClick(data[position])
        }
    }

    fun submitData(newData: List<ServersItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = data.size

            override fun getNewListSize() = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].localId == newData[newItemPosition].localId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        })
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class HolderVM(private val binding: ItemVmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ServersItem) {
            binding.tvName.text = data.name
            binding.tvLocation.text = data.location
            binding.tvImage.text = data.templateLabel
            binding.tvIPAddress.text = data.publicIp
            binding.tvStatus.text = data.status
        }
    }
}
