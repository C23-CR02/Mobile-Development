package com.bangkit.cloudraya.ui.SiteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.databinding.FragmentSiteListBinding

class FragmentSiteList : Fragment() {
    private lateinit var binding : FragmentSiteListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toHome()
//        toSiteEdit()
    }

    private fun toSiteEdit(){
        val toSiteEdit = FragmentSiteListDirections.actionFragmentSiteListToFragmentSiteAdd2()
        findNavController().navigate(toSiteEdit)
    }

    private fun toHome(){
        val toHome = FragmentSiteListDirections.actionFragmentSiteListToHomeFragment()
        findNavController().navigate(toHome)
    }
}