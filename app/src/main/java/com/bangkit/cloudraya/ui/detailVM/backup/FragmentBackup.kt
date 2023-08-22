package com.bangkit.cloudraya.ui.detailVM.backup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.cloudraya.databinding.FragmentBackupBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentBackup : Fragment() {
    private lateinit var binding: FragmentBackupBinding
    private val viewModel : BackupViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData(){
        val token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()
        val siteUrl = arguments?.getString(FragmentDetailVM.ARG_SITEURL).toString()

        viewModel.getBackupList(token,siteUrl).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    Log.d("Testing","response : ${result.data.data}")
                }
                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                else -> {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        }
    }
}