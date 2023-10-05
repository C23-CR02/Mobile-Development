package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.databinding.ItemIpPublicBinding
import com.bangkit.cloudraya.model.remote.PublicIpsItem

class IpPublicAdapter(private val listIp : List<PublicIpsItem>) :
    RecyclerView.Adapter<IpPublicAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ItemIpPublicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PublicIpsItem) {
            if(data.isPrimary == 1) {
                binding.btnDetach.alpha = 0.7F
                binding.btnDetach.isEnabled = false
            }
            binding.tvIpPublic.text = data.jsonMemberPublic
            binding.tvIpPrivate.text = data.jsonMemberPrivate
            binding.tvCost.text = data.cost.toString()
            binding.btnDetach.setOnClickListener {
                onDetachClickListener?.invoke(data.publicIpId)
            }
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

    private var onDetachClickListener: ((vmId: Int) -> Unit)? = null

    fun setOnDetachClickListener(listener: (vmId: Int) -> Unit) {
        onDetachClickListener = listener
    }

}