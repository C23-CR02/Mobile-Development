package com.bangkit.cloudraya.ui.detailVM.backup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.databinding.FragmentBackupBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.BackupConfigData
import com.bangkit.cloudraya.ui.adapter.BackupAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentBackup : Fragment() {
    private lateinit var binding: FragmentBackupBinding
    private val viewModel : BackupViewModel by viewModel()
    private lateinit var backupAdapter: BackupAdapter
    private var token = ""
    private var siteUrl = ""
    private var vmId = 0
    private lateinit var pDialog: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()
        siteUrl = arguments?.getString(FragmentDetailVM.ARG_SITEURL).toString()
        vmId = arguments?.getInt(FragmentDetailVM.ARG_VM_ID) ?: 0


        setRecyclerView()
        observeData()

        binding.btnSaveConfig.setOnClickListener {
            val status = if (binding.swBackup.isChecked) 1 else 0
            val days = binding.swDays.selectedItemPosition + 1
            val retentions = binding.swRetentions.selectedItemPosition + 1
            saveBackupConfig(status, days, retentions)
        }
    }

    private fun observeData(){
        viewModel.getBackupConfig(token, vmId).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    updateConfig(result.data.data)
                    Log.d("Testing","config : ${result.data.data}")
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
        viewModel.getBackupList(token,siteUrl).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    backupAdapter.submitData(result.data.data!!.snapshots)
                    Log.d("Testing","response : ${result.data.data}")
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                is Event.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Snackbar.make(binding.root, result.error ?: "Error", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setRecyclerView(){
        backupAdapter = BackupAdapter(
            { backupData ->
                viewModel.deleteBackup(token, backupData.backupId, vmId)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Event.Success -> {
                                pDialog.dismiss()
                                Log.d("Testing","response : ${result.data.data}")
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Successful!")
                                    .setContentText(result.data.message)
                                    .show()
                            }
                            is Event.Error -> {
                                pDialog.dismiss()
                                Log.d("Testing","response : ${result.error}")
                                SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(result.error)
                                    .show()
                            }
                            is Event.Loading -> {
                                pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                                pDialog.titleText = "Loading"
                                pDialog.setCancelable(false)
                                pDialog.show()
                            }
                        }
                    }
            },
            { backupData ->
                viewModel.restoreBackup(token, backupData.backupId, vmId)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Event.Success -> {
                                pDialog.dismiss()
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Successful!")
                                    .setContentText(result.data.message)
                                    .show()
                            }
                            is Event.Error -> {
                                pDialog.dismiss()
                                SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(result.error)
                                    .show()
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
        )
        binding.rvBackupList.apply {
            adapter = backupAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun saveBackupConfig(status: Int, days: Int, retentions: Int){
        viewModel.saveBackupConfig(token, vmId, status, days, retentions).observe(viewLifecycleOwner){ result ->
            when (result) {
                is Event.Success -> {
                    pDialog.dismiss()
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Successful!")
                        .setContentText(result.data.message)
                        .show()
                }
                is Event.Error -> {
                    pDialog.dismiss()
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(result.error)
                        .show()
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

    private fun updateConfig(backupConfig: BackupConfigData){
        binding.swBackup.isChecked = backupConfig.status == "Enabled"
        binding.swDays.setSelection(backupConfig.days - 1)
        binding.swRetentions.setSelection(backupConfig.retentions - 1)
    }
}