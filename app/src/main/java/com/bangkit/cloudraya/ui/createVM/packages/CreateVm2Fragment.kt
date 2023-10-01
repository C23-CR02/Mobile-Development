package com.bangkit.cloudraya.ui.createVM.packages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.cloudraya.databinding.FragmentCreateVm2Binding


class CreateVm2Fragment : Fragment() {
    private lateinit var binding: FragmentCreateVm2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateVm2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }
}