package com.bangkit.cloudraya.ui.resources

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.databinding.FragmentResourcesBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.ui.adapter.VMAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentResources : Fragment() {
    private lateinit var binding: FragmentResourcesBinding
    private val viewModel: ResourcesViewModel by viewModel()
    private lateinit var vmAdapter: VMAdapter
    private lateinit var site: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecycleView()
        site = arguments?.getString("data") ?: ""
        val data = viewModel.getListEncrypted(site)
        val token = data[2].toString()

        lifecycleScope.launch {
            getVmlist(token)
        }

        getToken()
        backPressed()
    }

    private fun getVmlist(token: String) {
        viewModel.getVMList(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    vmAdapter.submitData(result.data.data!!)
                    if (result.data.data.isEmpty()) {
                        binding.rvVM.visibility = View.GONE
                        binding.ivEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rvVM.visibility = View.VISIBLE
                        binding.ivEmpty.visibility = View.GONE
                    }
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showRecycleView() {
        vmAdapter = VMAdapter() { vmData ->
            val toDetailVM =
                FragmentResourcesDirections.actionFragmentResourcesToFragmentDetailVM(vmData, site)
            findNavController().navigate(toDetailVM)
        }
        binding.rvVM.apply {
            adapter = vmAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getToken() {
        val data = viewModel.getListEncrypted(site)
        val appKey = data[0].toString()
        val appSecret = data[1].toString()
        val request = JsonObject().apply {
            addProperty("app_key", appKey)
            addProperty("secret_key", appSecret)
        }
        viewModel.getToken(request).observe(viewLifecycleOwner) { item ->
            when (item) {
                is Event.Success -> {
                    val token = "Bearer ${item.data.data?.bearerToken.toString()}"
                    lifecycleScope.launch {
                        val list = listOf(appKey, appSecret, token)
                        viewModel.saveListEncrypted(site, list)
                    }


                }
                is Event.Error -> {
                    Log.d("Calling error : ", item.error.toString())
                }
                else -> {
                    Log.d("Event ", item.toString())
                }
            }
        }
    }

    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toSiteList()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun toSiteList() {
        val toSiteList = FragmentResourcesDirections.actionFragmentResourcesToFragmentSiteList()
        findNavController().navigate(toSiteList)
    }
}