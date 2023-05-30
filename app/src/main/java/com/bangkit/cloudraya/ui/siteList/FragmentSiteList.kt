package com.bangkit.cloudraya.ui.siteList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteListBinding
import com.bangkit.cloudraya.ui.siteList.SiteListViewModel
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
        backPressed()
    }


    private fun toDetailVM() {
        lifecycleScope.launch {
            val site = viewModel.getSites()
            val adapter = SiteListAdapter(site)
            val layoutManager = LinearLayoutManager(requireContext())
            adapter.setOnItemClickCallback(object : SiteListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Sites) {
                    showSelectedSite(data)
                }

            })
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layoutManager
            if (site.isEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.ivEmpty.visibility = View.VISIBLE
            }else{
                binding.recyclerView.visibility = View.VISIBLE
                binding.ivEmpty.visibility = View.GONE
            }
        }
    }


    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.exit_confirmation))
            .setMessage(getString(R.string.exit_confirmation_message))
            .setPositiveButton(getString(R.string.exit)) { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    private fun showSelectedSite(data : Sites) {
        viewModel.setBaseUrl(data.site_url)
        val toResource = FragmentSiteListDirections.actionFragmentSiteListToHomeFragment(data.site_name)
        findNavController().navigate(toResource)
    }

    private fun toSiteAdd() {
        val toSiteEdit = FragmentSiteListDirections.actionFragmentSiteListToFragmentSiteAdd()
        findNavController().navigate(toSiteEdit)
    }


}