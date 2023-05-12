package com.bangkit.cloudraya.ui.SiteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteListBinding
import com.bangkit.cloudraya.ui.adapter.SiteListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSiteList : Fragment() {
    private lateinit var binding: FragmentSiteListBinding
    private val viewModel : SiteListViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterSite.setOnClickListener {
            toSiteAdd()
        }
        toDetailVM()
    }


    private fun toDetailVM() {
        lifecycleScope.launch {
            val site = viewModel.getSites()
            val adapter = SiteListAdapter(site)
            val layoutManager = LinearLayoutManager(requireContext())
            adapter.setOnItemClickCallback(object : SiteListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Sites) {
                    showSelectedSite()
                }

            })
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layoutManager
        }
    }

    private fun showSelectedSite() {
        val toResource = FragmentSiteListDirections.actionFragmentSiteListToHomeFragment()
        findNavController().navigate(toResource)
    }

    private fun toSiteAdd() {
        val toSiteEdit = FragmentSiteListDirections.actionFragmentSiteListToFragmentSiteAdd()
        findNavController().navigate(toSiteEdit)
    }

    private fun toHome() {
        val toHome = FragmentSiteListDirections.actionFragmentSiteListToHomeFragment()
        findNavController().navigate(toHome)
    }
}