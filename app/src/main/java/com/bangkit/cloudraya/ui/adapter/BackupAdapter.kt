package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemBackupBinding
import com.bangkit.cloudraya.model.remote.SnapshotsItem

class BackupAdapter(private val backupList : List<SnapshotsItem>): RecyclerView.Adapter<BackupAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding : ItemBackupBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind(data : SnapshotsItem) {
            binding.tvName.text = data.backupName
            binding.tvSize.text = data.size
            binding.tvDate.text = data.created

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBackupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() : Int = backupList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = backupList[position]
        holder.bind(data)

    }
}