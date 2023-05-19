package com.bangkit.cloudraya.ui.SiteAdd

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteAddBinding
import com.bangkit.cloudraya.model.local.Event
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSiteAdd : Fragment() {
    private lateinit var binding : FragmentSiteAddBinding
    private var token : String = "Bearer "
    private val viewModel : SiteAddViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSiteAddBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            if (isFilled()) {
                addSite()
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

    private fun toList(){
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
        val appKey = binding.appKeyLayout.text.toString()
        val appSecret = binding.appSecretLayout.text.toString()

        val request = JsonObject().apply {
            addProperty("app_key", appKey)
            addProperty("secret_key", appSecret)
        }
        viewModel.getToken(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    token += data.data.data?.bearerToken.toString()
                    val site = Sites(
                        siteName,
                        siteUrl,
                        appKey,
                        appSecret,
                        token
                    )
                    lifecycleScope.launch{
                        viewModel.insertSites(site)
                        viewModel.saveEncrypted(appKey, appSecret, token)
                        val list = listOf(appKey,appSecret,token)
                        viewModel.saveListEncrypted(siteName,list)
                        viewModel.getListEncrypted(siteName)
                        callToast()
                        delay(Toast.LENGTH_SHORT.toLong())
                        toList()
                    }


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
    private fun callToast(){
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val layout: View = inflater.inflate(R.layout.success_toast, requireActivity().findViewById(R.id.toast_successful))
        val toast = Toast(requireContext())
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}