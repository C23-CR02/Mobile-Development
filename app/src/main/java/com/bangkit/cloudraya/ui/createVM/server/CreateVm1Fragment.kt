package com.bangkit.cloudraya.ui.createVM.server

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentCreateVm1Binding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.TemplatesItem
import com.bangkit.cloudraya.ui.adapter.TemplateAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateVm1Fragment : Fragment() {
    private lateinit var binding: FragmentCreateVm1Binding
    private val viewModel: CreateVm1ViewModel by viewModel()
    private lateinit var templateAdapter: TemplateAdapter
    private var token = ""
    private var location = ""
    private val locationList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateVm1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()

        setRecyclerView()
        fetchLocation()

        binding.btnNext.setOnClickListener {
            val toCreateVM2 = CreateVm1FragmentDirections.actionCreateVm1FragmentToCreateVm2Fragment(token)
            findNavController().navigate(toCreateVM2)
        }
    }

    private fun setRecyclerView(){
        templateAdapter = TemplateAdapter()
        binding.rvServer.apply {
            adapter = templateAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchLocation(){
        viewModel.getLocationList(token).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {

                    result.data.data?.id?.let {
                        it.forEach {item ->
                            if (item != null){
                                locationList.add(item.location!!)
                            }
                        }
                    }
                    result.data.data?.us?.let {
                        it.forEach {item ->
                            if (item != null){
                                locationList.add(item.location!!)
                            }
                        }
                    }
                    setLocationAdapter()
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

    private fun setLocationAdapter(){
        if (locationList.isNotEmpty()){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, locationList)
            binding.locationInput.apply {
                setAdapter(arrayAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    location = locationList[position]
                    fetchTemplate()
                }
            }
        }
    }

    private fun fetchTemplate(){
        viewModel.getTemplateList(token, location).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    Log.d("Testing","template : ${result.data.data?.templates}")
                    templateAdapter.submitData(result.data.data?.templates as List<TemplatesItem>)
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