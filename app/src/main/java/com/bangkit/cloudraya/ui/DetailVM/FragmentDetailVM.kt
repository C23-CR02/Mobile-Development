package com.bangkit.cloudraya.ui.DetailVM

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.model.remote.VMListData
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    private val viewModel: DetailVMViewModel by viewModel()
    private lateinit var vmData: VMListData
    private val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBpLmNsb3VkcmF5YS5jb21cL3YxXC9hcGlcL2dhdGV3YXlcL3VzZXJcL2F1dGgiLCJpYXQiOjE2ODM4MTg0MDIsImV4cCI6MTY4MzgyNTYwMiwibmJmIjoxNjgzODE4NDAyLCJqdGkiOiJKWjRKbnY4aWg3OGZWTkVpIiwic3ViIjo3NywicHJ2IjoiYTU1NDE1NDk1MDQ1ODI1YzVlZTQ3NWMzMTZhYWVjOWRjMjYzZmE5MiJ9.rE9FrDCv8zY4VngVs2JLbyrpdBXbGavNTyj-BPl7JJI"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmData = FragmentDetailVMArgs.fromBundle(arguments as Bundle).vmData
        observeData(vmData)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnStart.setOnClickListener {
            startVM()
        }
        binding.btnStop.setOnClickListener {
            stopVM()
        }
        binding.btnRestart.setOnClickListener {
            restartVM()
        }
    }

    private fun observeData(vmData: VMListData){
        Log.d("Request Detail", vmData.localId.toString())
        viewModel.getVMDetail(token,vmData.localId!!).observe(viewLifecycleOwner){ result ->
            when(result){
                is Event.Success -> {
                    updateUI(result.data.data!!)
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun startVM(){
        viewModel.vmAction(token, vmData.localId!!, "start").observe(viewLifecycleOwner){ result ->
            when(result){
                is Event.Success -> {
                    Snackbar.make(binding.root, "${result.data.data} - ${result.data.message}" , Snackbar.LENGTH_LONG).show()
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun stopVM(){
        viewModel.vmAction(token, vmData.localId!!, "stop").observe(viewLifecycleOwner){ result ->
            when(result){
                is Event.Success -> {
                    Snackbar.make(binding.root, "${result.data.data} - ${result.data.message}" , Snackbar.LENGTH_LONG).show()
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun restartVM(){
        viewModel.vmAction(token, vmData.localId!!, "reboot").observe(viewLifecycleOwner){ result ->
            when(result){
                is Event.Success -> {
                    Snackbar.make(binding.root, "${result.data.data} - ${result.data.message}" , Snackbar.LENGTH_LONG).show()
                }
                is Event.Error -> {
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT).show()
                }
                is Event.Loading -> {

                }
            }
        }
    }

    private fun updateUI(data: VMData){
        binding.tvName.text = data.hostname
        binding.tvLocation.text = data.location
        binding.tvImage.text = data.os
        binding.tvIPAddress.text = data.publicIp
        binding.tvLaunchDate.text = data.launchDate
        binding.tvStatus.text = data.state

    }
}