package com.bangkit.cloudraya.ui.Resources

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var token: String = "Bearer "
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

        binding.btnHome.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {

            val appKey = viewModel.getSites()[0].app_key
            val appSecret = viewModel.getSites()[0].secret_key
            val request = JsonObject().apply {
                addProperty("app_key", appKey)
                addProperty("secret_key", appSecret)
            }
            viewModel.getToken(request).observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Event.Success -> {
                        token += data.data.data?.bearerToken
                        getVmlist(token)
                    }
                    else -> {
                        Log.d("Event ", data.toString())
                    }
                }
            }


        }
    }
    private fun getVmlist(token : String){
        viewModel.getVMList(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    vmAdapter.submitData(result.data.data!!)
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun showRecycleView() {
        vmAdapter = VMAdapter() { vmData ->
            val toDetailVM =
                FragmentResourcesDirections.actionFragmentResourcesToFragmentDetailVM(vmData)
            findNavController().navigate(toDetailVM)
        }
        binding.rvVM.apply {
            adapter = vmAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}