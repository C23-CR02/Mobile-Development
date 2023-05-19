package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.ItemSiteBinding

class SiteListAdapter(private var sites: List<Sites>) :
    RecyclerView.Adapter<SiteListAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ItemSiteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Sites) {
            binding.tvSiteName.text = data.site_name
            binding.tvSiteUrl.text = data.site_url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = sites.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = sites[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(sites[holder.adapterPosition])}
    }
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback =onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Sites)
    }
}