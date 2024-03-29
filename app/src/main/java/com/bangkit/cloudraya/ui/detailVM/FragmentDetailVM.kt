package com.bangkit.cloudraya.ui.detailVM

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.VMData
import com.bangkit.cloudraya.ui.adapter.DetailMenuAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    private val viewModel: DetailVMViewModel by viewModel()
    private var site: String? = null
    private lateinit var siteUrl: String
    private lateinit var vmId: String
    private var loc = ""
    private var token = ""
    private var fromNotif: Boolean = false
    private lateinit var pDialog: SweetAlertDialog
    private val dataHolder: DataHolder by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmId = FragmentDetailVMArgs.fromBundle(arguments as Bundle).vmId
        site = arguments?.getString("site") ?: ""
        siteUrl = arguments?.getString("siteUrl") ?: ""
        if (dataHolder.vmId != "" && site == "") {
            token = "bearer ${dataHolder.token}"
            vmId = dataHolder.vmId
            siteUrl = dataHolder.siteUrl
            fromNotif = true
            dataHolder.clearData()

        } else if (site != null) {
            val data = viewModel.getListEncrypted(site!!)
            token = data[2].toString()
        }
        observeData(vmId)
        binding.btnBack.setOnClickListener {
            goBack()
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
        backPressed()
    }

    private fun observeData(vmId: String) {
        viewModel.setBaseUrl(siteUrl)
        viewModel.getVMDetail(token, vmId.toInt())
        viewModel.vmDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    updateUI(result.data.data!!)
                    loc = result.data.data.location.toString()
                    setPager()
                }

                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }

                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        }

    }


    private fun startVM() {
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmId.toInt(), "start").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.SUCCESS_TYPE
                        ).setTitleText("Successful!").setContentText(result.data.message).show()
                        updateStatus("Running")
                    }

                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.ERROR_TYPE
                        ).setTitleText("Oops...").setContentText(result.error).show()
                    }

                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun stopVM() {
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmId.toInt(), "stop").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.SUCCESS_TYPE
                        ).setTitleText("Successful!").setContentText(result.data.message).show()
                        updateStatus("Stopped")
                    }

                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.ERROR_TYPE
                        ).setTitleText("Oops...").setContentText(result.error).show()
                    }

                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun restartVM() {
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmId.toInt(), "reboot").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.SUCCESS_TYPE
                        ).setTitleText("Successful!").setContentText(result.data.message).show()
                        updateStatus("Running")
                    }

                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(
                            requireContext(),
                            SweetAlertDialog.ERROR_TYPE
                        ).setTitleText("Oops...").setContentText(result.error).show()
                    }

                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
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
            tvCPU.text = "${data.cpu} Core"
            tvTypeDisc.text = data.disk
            tvMemory.text = "${data.memory} MB"
            tvPackage.text = data.vmPackage
            tvLaunchDate.text = data.launchDate
            tvStatus.text = data.state
        }
        if (data.state!!.contains("stop", ignoreCase = true)) {
            binding.apply {
                btnStop.isEnabled = false
                btnStart.isEnabled = true
                btnRestart.isEnabled = false
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive, 0, 0, 0)
            }
        } else {
            binding.apply {
                btnStop.isEnabled = true
                btnStart.isEnabled = false
                btnRestart.isEnabled = true
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_active, 0, 0, 0)
            }
        }
    }

    private fun setPager() {
        val pagerAdapter = DetailMenuAdapter(activity as AppCompatActivity)
        pagerAdapter.setValue(token, vmId.toInt(), siteUrl, loc)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (fromNotif) {
                    goMenu()
                } else {
                    goBack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun goMenu() {
        val menu = FragmentDetailVMDirections.actionFragmentDetailVMToFragmentSiteList()
        findNavController().navigate(menu)
    }

    private fun goBack() {
        val back = FragmentDetailVMDirections.actionFragmentDetailVMToFragmentResources(
            site!!, siteUrl
        )
        findNavController().navigate(back)
    }

    private fun updateStatus(status: String) {
        binding.tvStatus.text = status
        if (status.contains("stop", ignoreCase = true)) {
            binding.apply {
                btnStop.isEnabled = false
                btnStart.isEnabled = true
                btnRestart.isEnabled = false
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_inactive, 0, 0, 0)
            }
        } else {
            binding.apply {
                btnStop.isEnabled = true
                btnStart.isEnabled = false
                btnRestart.isEnabled = true
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_active, 0, 0, 0)
            }
        }
    }

    companion object {
        val TAB_TITLES = listOf("Monitoring", "Backup", "IP")
        const val ARG_TOKEN = "token"
        const val ARG_VM_ID = "vm_id"
        const val ARG_SITEURL = "siteUrl"
        const val ARG_LOC = "loc"
    }
}