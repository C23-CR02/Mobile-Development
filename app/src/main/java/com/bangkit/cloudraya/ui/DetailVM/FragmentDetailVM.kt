package com.bangkit.cloudraya.ui.DetailVM

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.model.remote.VMListData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    private val viewModel: DetailVMViewModel by viewModel()
    private lateinit var vmData: VMListData
    private lateinit var site: String
    private var token = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmData = FragmentDetailVMArgs.fromBundle(arguments as Bundle).dataVM
        site = arguments?.getString("site") ?: ""
        val data = viewModel.getListEncrypted(site)
        token = data[2].toString()

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

    private fun observeData(vmData: VMListData) {
        Log.d("Request Detail", vmData.localId.toString())
        viewModel.getVMDetail(token, vmData.localId!!)
        viewModel.vmDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    updateUI(result.data.data!!)
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


    private fun startVM() {
        viewModel.vmAction(token, vmData.localId!!, "start")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        Snackbar.make(
                            binding.root,
                            "${result.data.data} - ${result.data.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.error ?: "Error",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is Event.Loading -> {

                    }
                }
            }
    }

    private fun stopVM() {
        viewModel.vmAction(token, vmData.localId!!, "stop")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        Snackbar.make(
                            binding.root,
                            "${result.data.data} - ${result.data.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.error ?: "Error",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Event.Loading -> {

                    }
                }
            }
    }

    private fun restartVM() {
        viewModel.vmAction(token, vmData.localId!!, "reboot")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        Snackbar.make(
                            binding.root,
                            "${result.data.data} - ${result.data.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                        observeData(vmData)
                    }
                    is Event.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.error ?: "Error",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    is Event.Loading -> {

                    }
                }
            }
    }

    private fun updateUI(data: VMData) {
        binding.apply {
            tvName.text = data.hostname
            tvLocation.text = data.location
            tvImage.text = data.os
            tvIPAddress.text = data.publicIp
            tvLaunchDate.text = data.launchDate
            tvStatus.text = data.state
        }
        if (data.state!!.contains("stop", ignoreCase = true)){
            binding.apply {
                btnStop.isEnabled = false
                btnStart.isEnabled = true
                btnRestart.isEnabled = false
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive, 0, 0, 0)
            }
        }else{
            binding.apply {
                btnStop.isEnabled = true
                btnStart.isEnabled = false
                btnRestart.isEnabled = true
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_active, 0, 0, 0)
            }
        }

    }
}