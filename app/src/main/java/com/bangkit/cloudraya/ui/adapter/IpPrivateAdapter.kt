package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemIpPrivateBinding
import com.bangkit.cloudraya.model.remote.IpPrivateItem

class IpPrivateAdapter(private val listIp: List<IpPrivateItem>) :
    RecyclerView.Adapter<IpPrivateAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemIpPrivateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IpPrivateItem) {
            binding.tvIp.text = data.ipAddress
            if(data.isDefault == false){
                binding.tvDefault.visibility = View.GONE
            } else {
                binding.btnDelete.isEnabled = false
                binding.btnDelete.alpha = 0.7F
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemIpPrivateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listIp.size
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listIp[position]
        holder.bind(data)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listIp[holder.adapterPosition]) }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: IpPrivateItem)
    }
}