package com.bangkit.cloudraya.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.databinding.FragmentHomeBinding
import com.bangkit.cloudraya.model.local.Event
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var site: String
    private var token: String = "Bearer "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        site = arguments?.getString("data") ?: ""
        binding.btnResources.setOnClickListener {
            val toResources = HomeFragmentDirections.actionHomeFragmentToFragmentResources(site)
            findNavController().navigate(toResources)
        }
        Log.d("Testing", site)

        getToken()
        backPressed()
    }

    private fun getToken() {
        val data = viewModel.getListEncrypted(site)
        val appKey = data[0].toString()
        val appSecret = data[1].toString()
        val request = JsonObject().apply {
            addProperty("app_key", appKey)
            addProperty("secret_key", appSecret)
        }
        viewModel.getToken(request).observe(viewLifecycleOwner) { item ->
            when (item) {
                is Event.Success -> {
                    token += item.data.data?.bearerToken.toString()
                    lifecycleScope.launch {
                        val list = listOf(appKey, appSecret, token)
                        viewModel.saveListEncrypted(site, list)
                    }


                }
                is Event.Error -> {
                    Log.d("Calling error : ", item.error.toString())
                }
                else -> {
                    Log.d("Event ", item.toString())
                }
            }
        }
    }

    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toSiteList()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun toSiteList() {
        val toSiteList = HomeFragmentDirections.actionHomeFragmentToFragmentSiteList()
        findNavController().navigate(toSiteList)
    }
}