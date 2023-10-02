package com.bangkit.cloudraya.ui.createVM.packages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.databinding.FragmentCreateVm2Binding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.ui.adapter.PackageAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateVm2Fragment : Fragment() {
    private lateinit var binding: FragmentCreateVm2Binding
    private lateinit var packageAdapter: PackageAdapter
    private val viewModel: CreateVm2ViewModel by viewModel()
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateVm2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()

        setRecyclerView()
        fetchPackages()
    }

    private fun setRecyclerView(){
        packageAdapter = PackageAdapter()
        binding.rvPackage.apply {
            adapter = packageAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchPackages(){
        viewModel.getPackages(token).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    packageAdapter.submitData(result.data.data)
                    Log.d("Testing","response : ${result.data.data}")
                }
                is Event.Loading -> {

                }
                is Event.Error -> {

                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}