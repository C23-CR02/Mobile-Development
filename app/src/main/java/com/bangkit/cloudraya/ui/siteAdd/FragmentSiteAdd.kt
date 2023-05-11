package com.bangkit.cloudraya.ui.siteAdd

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteAddBinding
import com.bangkit.cloudraya.model.local.Event
import com.bangkit.cloudraya.network.BaseUrlProvider
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSiteAdd : Fragment() {
    private lateinit var binding: FragmentSiteAddBinding
    private val viewModel: SiteAddViewModel by viewModel()
    private val baseUrlProvider: BaseUrlProvider by inject()
    private var token: String = "bearer "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            Log.d("testing", "${isFilled()}")
            if (isFilled()) {
                addSite()
                toList()
            } else {
                binding.apply {
                    siteNameLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        siteNameLayout.error = "Insert your site name"
                    }
                    siteUrlLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        siteUrlLayout.error = "Insert your site url"
                    }
                    appKeyLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        appKeyLayout.error = "Insert your app key"
                    }
                    appSecretLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        appSecretLayout.error = "Insert your secret key"
                    }
                }
            }
        }

    }

    private fun toList() {
        val toList = FragmentSiteAddDirections.actionFragmentSiteAddToFragmentSiteList()
        findNavController().navigate(toList)
    }

    private fun isFilled(): Boolean {
        binding.apply {
            val siteName = siteNameLayout.text
            val siteUrl = siteUrlLayout.text
            val appKey = appKeyLayout.text
            val secretKey = appSecretLayout.text
            return !siteName.isNullOrEmpty() && !siteUrl.isNullOrEmpty() && !appKey.isNullOrEmpty() && !secretKey.isNullOrEmpty()
        }
    }

    private fun addSite() {
        val siteName = binding.siteNameLayout.text.toString()
        val siteUrl = binding.siteUrlLayout.text.toString()
        baseUrlProvider.setBaseUrl(siteUrl)
        val appKey = binding.appKeyLayout.text.toString()
        val appSecret = binding.appSecretLayout.text.toString()

        val request = JsonObject().apply {
            addProperty("app_key", appKey)
            addProperty("secret_key", appSecret)
        }
        Log.d("testing", "site url $siteUrl")

        Log.d("Testing ", "response : $request")

        viewModel.getToken(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    Log.d("Check Response Data : ", data.toString())
                    token = data.data.data?.bearerToken.toString()
                    val site = Sites(
                        siteName,
                        siteUrl,
                        appKey,
                        appSecret,
                        token
                    )
                    lifecycleScope.launch {
                        viewModel.insertSites(site)
                    }
                    Toast.makeText(
                        requireContext(), data.data.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Event.Error -> {
                    Log.d("Calling error : ", data.error.toString())
                }
                else -> {
                    Log.d("Event ", data.toString())
                }
            }

        }
    }


}