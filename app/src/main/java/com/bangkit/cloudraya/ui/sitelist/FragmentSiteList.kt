package com.bangkit.cloudraya.ui.sitelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.databinding.FragmentSiteListBinding
import com.bangkit.cloudraya.ui.adapter.SiteListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSiteList : Fragment() {
    private lateinit var binding: FragmentSiteListBinding
    private val viewModel: SiteListViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSites()
        binding.btnRegisterSite.setOnClickListener {
            toSiteEdit()
        }
    }

    private fun getSites() {
        lifecycleScope.launch {
            val site = viewModel.getSites()
            Log.d("testing", "site $site")
            val adapter = SiteListAdapter(site)
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
        }
    }

    private fun toSiteEdit() {
        val toSiteEdit = FragmentSiteListDirections.actionFragmentSiteListToFragmentSiteAdd2()
        findNavController().navigate(toSiteEdit)
    }
}