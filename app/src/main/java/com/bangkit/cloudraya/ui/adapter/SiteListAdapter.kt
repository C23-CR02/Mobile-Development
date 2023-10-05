package com.bangkit.cloudraya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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
    fun updateData(newSites : List<Sites>){
        val diffResult = DiffUtil.calculateDiff(SiteDiffCallback(sites, newSites))
        sites = newSites
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = sites.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = sites[position]
        holder.bind(data)
        holder.itemView.setOnLongClickListener {
            onItemClickLongCallback.onItemLongClicked(sites[holder.adapterPosition])
            true
        }
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(sites[holder.adapterPosition])}
    }
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onItemClickLongCallback: OnItemClickLongCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback =onItemClickCallback
    }

    fun setOnItemLongClickCallback(onItemClickLongCallback : OnItemClickLongCallback){
        this.onItemClickLongCallback = onItemClickLongCallback
    }
    interface OnItemClickLongCallback {
        fun onItemLongClicked(data : Sites)
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Sites)
    }

    private inner class SiteDiffCallback(
        private val oldSites: List<Sites>,
        private val newSites: List<Sites>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldSites.size

        override fun getNewListSize(): Int = newSites.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSite = oldSites[oldItemPosition]
            val newSite = newSites[newItemPosition]
            return oldSite.site_name == newSite.site_name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldSite = oldSites[oldItemPosition]
            val newSite = newSites[newItemPosition]
            return oldSite.site_name == newSite.site_name && oldSite.site_url == newSite.site_url
        }
    }
}