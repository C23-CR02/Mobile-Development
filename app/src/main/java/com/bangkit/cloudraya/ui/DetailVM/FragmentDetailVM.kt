package com.bangkit.cloudraya.ui.DetailVM

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.cloudraya.databinding.FragmentDetailVmBinding

class FragmentDetailVM : Fragment() {
    private lateinit var binding: FragmentDetailVmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailVmBinding.inflate(inflater,container,false)
        return binding.root
    }
}