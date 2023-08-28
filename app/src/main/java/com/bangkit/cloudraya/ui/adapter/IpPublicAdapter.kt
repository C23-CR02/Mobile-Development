package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemIpPublicBinding
import com.bangkit.cloudraya.model.remote.DataItem

class IpPublicAdapter(private val listIp : List<DataItem>) :
    RecyclerView.Adapter<IpPublicAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemIpPublicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            binding.tvIp.text = data.publicIp
            binding.tvIpPublic.text = data.publicIp
            binding.tvLocation.text = data.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemIpPublicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listIp.size
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listIp[position]
        holder.bind(data)
    }
}