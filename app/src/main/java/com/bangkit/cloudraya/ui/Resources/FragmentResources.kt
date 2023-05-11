package com.bangkit.cloudraya.ui.Resources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.cloudraya.databinding.FragmentResourcesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentResources : Fragment() {
    private lateinit var binding : FragmentResourcesBinding
    private val viewModel: ResourcesViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourcesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.print("hello")
    }
}