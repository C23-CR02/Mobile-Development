package com.bangkit.cloudraya.ui.DetailVM

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.model.remote.VMListData
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    private val viewModel: DetailVMViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vmData = FragmentDetailVMArgs.fromBundle(arguments as Bundle).vmData
        observeData(vmData)

        binding.btnStart.setOnClickListener {
            viewModel.vmAction("", vmData.localId!!, "start")
        }
        binding.btnStop.setOnClickListener {
            viewModel.vmAction("", vmData.localId!!, "stop")
        }
        binding.btnRestart.setOnClickListener {
            viewModel.vmAction("", vmData.localId!!, "reboot")
        }
    }
    private fun observeData(vmData: VMListData){
        viewModel.getVMDetail("",vmData.localId!!).observe(viewLifecycleOwner){ result ->
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

    private fun updateUI(data: VMData){
        binding.tvName.text = data.hostname
        binding.tvLocation.text = data.location
        binding.tvImage.text = data.os
        binding.tvIPAddress.text = data.publicIp
        binding.tvLaunchDate.text = data.launchDate
        binding.tvStatus.text = data.state

    }
}