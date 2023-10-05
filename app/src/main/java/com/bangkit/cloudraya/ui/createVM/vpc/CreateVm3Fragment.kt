package com.bangkit.cloudraya.ui.createVM.vpc

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentCreateVm3Binding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.model.remote.UserVpcNetworksItem
import com.bangkit.cloudraya.model.remote.VPCItem
import com.bangkit.cloudraya.ui.detailVM.FragmentDetailVM
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateVm3Fragment : Fragment() {
    private lateinit var binding : FragmentCreateVm3Binding
    private val viewModel: CreateVm3ViewModel by viewModel()
    private lateinit var vpcList: List<VPCItem>
    private lateinit var vpcNetworkList: List<UserVpcNetworksItem>
    private var token = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateVm3Binding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = arguments?.getString(FragmentDetailVM.ARG_TOKEN).toString()

        val vpcSettingOption = listOf("Existing VPC", "Custom VPC")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, vpcSettingOption)
        binding.acVpcSetting.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                if (position == 0){
                    binding.layoutExistingVpc.visibility = View.VISIBLE
                    binding.layoutCustomVpc.visibility = View.GONE
                }else{
                    binding.layoutExistingVpc.visibility = View.GONE
                    binding.layoutCustomVpc.visibility = View.VISIBLE
                }
            }
        }

        fetchVPC()
    }

    private fun fetchVPC(){
        viewModel.getVPC(token).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Event.Success -> {
                    vpcList = result.data.data
                    setVPCAdapter()
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

    private fun setVPCAdapter(){
        val vpcNames: MutableList<String> = mutableListOf()
        vpcList.forEach {
            vpcNames.add("${it.name} - ${it.networkAddress}")
        }
        if (vpcNames.isNotEmpty()){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, vpcNames)
            binding.acVpc.apply {
                setAdapter(arrayAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    vpcNetworkList = vpcList[position].userVpcNetworks
                    setSubnetAdapter()
                }
            }
        }
    }

    private fun setSubnetAdapter(){
        val vpcNetworks: MutableList<String> = mutableListOf()
        vpcNetworkList.forEach {
            vpcNetworks.add("${it.name} - ${it.networkAddress}")
        }
        if (vpcNetworks.isNotEmpty()){
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, vpcNetworks)
            binding.acVpcSubnet.apply {
                setAdapter(arrayAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    Toast.makeText(requireContext(),vpcNetworkList[position].name,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}