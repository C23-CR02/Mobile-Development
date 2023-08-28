package com.bangkit.cloudraya.ui.detailVM.ip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cloudraya.databinding.FragmentIpBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.ui.adapter.IpPrivateAdapter
import com.bangkit.cloudraya.ui.adapter.IpPublicAdapter
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentIP : Fragment() {
    private lateinit var binding: FragmentIpBinding
    private val viewModel: IpViewModel by viewModel()
    private lateinit var token: String
    private lateinit var siteUrl: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()
        siteUrl = arguments?.getString(FragmentDetailVM.ARG_SITEURL).toString()
        val vmId = arguments?.getInt(FragmentDetailVM.ARG_VM_ID)

        viewModel.getIpPrivate(token, siteUrl, vmId!!).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    val adapter = IpPrivateAdapter(result.data.data!!)
                    binding.rvIpPrivate.adapter = adapter
                    binding.rvIpPrivate.layoutManager = LinearLayoutManager(requireContext())
                }

                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }

                else -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.getIpPublic(token, siteUrl).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Event.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    val adapter = IpPublicAdapter(result.data.data)
                    binding.rvIpPublic.adapter = adapter
                    binding.rvIpPublic.layoutManager = LinearLayoutManager(requireContext())

                }

                is Event.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }

                else -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


}