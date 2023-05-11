package com.bangkit.cloudraya.ui.Resources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.databinding.FragmentResourcesBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.ui.adapter.VMAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentResources : Fragment() {
    private lateinit var binding : FragmentResourcesBinding
    private val viewModel: ResourcesViewModel by viewModel()
    private lateinit var vmAdapter: VMAdapter
    private val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLmNsb3VkcmF5YS5jb21cL3YxXC9hcGlcL2dhdGV3YXlcL3VzZXJcL2F1dGgiLCJpYXQiOjE2ODM4MTg0MDIsImV4cCI6MTY4MzgyNTYwMiwibmJmIjoxNjgzODE4NDAyLCJqdGkiOiJKWjRKbnY4aWg3OGZWTkVpIiwic3ViIjo3NywicHJ2IjoiYTU1NDE1NDk1MDQ1ODI1YzVlZTQ3NWMzMTZhYWVjOWRjMjYzZmE5MiJ9.rE9FrDCv8zY4VngVs2JLbyrpdBXbGavNTyj-BPl7JJI"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourcesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecycleView()

        binding.btnHome.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getVMList(token).observe(viewLifecycleOwner){ result ->
            when(result){
                is Event.Success -> {
                    vmAdapter.submitData(result.data.data!!)
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun showRecycleView(){
        vmAdapter = VMAdapter(){ vmData ->
            val toDetailVM = FragmentResourcesDirections.actionFragmentResourcesToFragmentDetailVM(vmData)
            findNavController().navigate(toDetailVM)
        }
        binding.rvVM.apply {
            adapter = vmAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}