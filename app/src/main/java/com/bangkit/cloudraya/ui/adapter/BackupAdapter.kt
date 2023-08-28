package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemBackupBinding
import com.bangkit.cloudraya.model.remote.SnapshotsItem

class BackupAdapter(
    private val onDeleteClick: (SnapshotsItem) -> Unit,
    private val onRestoreClick: (SnapshotsItem) -> Unit
) : RecyclerView.Adapter<BackupAdapter.MyViewHolder>() {
    private var data: List<SnapshotsItem> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBackupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)

    }

    fun submitData(newData: List<SnapshotsItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = data.size

            override fun getNewListSize() = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].backupId == newData[newItemPosition].backupId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        })
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: ItemBackupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SnapshotsItem) {
            binding.tvName.text = data.backupName
            binding.tvSize.text = data.size
            binding.tvDate.text = data.created
            binding.btnDelete.setOnClickListener {
                onDeleteClick(data)
            }
            binding.btnRestore.setOnClickListener {
                onRestoreClick(data)
            }
        }
    }
}
