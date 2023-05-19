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
        Log.d("Testing","site : $site")
        val data = viewModel.getListEncrypted(site)
        val token = data[2].toString()
        Log.d("Testing","token : $token")

        binding.btnHome.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            getVmlist(token)
        }
    }

    private fun getVmlist(token: String) {
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
                FragmentResourcesDirections.actionFragmentResourcesToFragmentDetailVM(vmData, site)
            findNavController().navigate(toDetailVM)
        }
        binding.rvVM.apply {
            adapter = vmAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}