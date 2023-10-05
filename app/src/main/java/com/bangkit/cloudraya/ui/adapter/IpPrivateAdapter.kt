package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemIpPrivateBinding
import com.bangkit.cloudraya.model.remote.PrivateIpsItem

class IpPrivateAdapter(
    private var listIp: List<PrivateIpsItem>,
) :
    RecyclerView.Adapter<IpPrivateAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ItemIpPrivateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PrivateIpsItem) {
            binding.tvIp.text = data.ipaddress
            if (data.isUsed == 0) {
                binding.tvDefault.visibility = View.GONE
                binding.btnDelete.setOnClickListener {
                    onDeleteClickListener?.invoke(data.id)
                }

            } else {
                if (data.isdefault == 1){
                    binding.tvDefault.visibility = View.VISIBLE
                }else{
                    binding.tvDefault.visibility = View.GONE
                }
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
    }

    private var onDeleteClickListener: ((dataId: String) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (dataId: String) -> Unit) {
        onDeleteClickListener = listener
    }
}