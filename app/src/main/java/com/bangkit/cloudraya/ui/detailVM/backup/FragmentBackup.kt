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

        observeData()
    }

    private fun observeData(){

        viewModel.getBackupList(token,siteUrl).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    backupAdapter.submitData(result.data.data.snapshots)
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
}