package com.bangkit.cloudraya.ui.siteAdd

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.MainActivity
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteAddBinding
import com.bangkit.cloudraya.websocket.WebSocketService
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.model.local.Event
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.MalformedURLException
import java.net.URL

class FragmentSiteAdd : Fragment() {
    private lateinit var binding: FragmentSiteAddBinding
    private var token: String = "Bearer "
    private val viewModel: SiteAddViewModel by viewModel()
    private lateinit var appKey: String
    private lateinit var appSecret: String
    private lateinit var webSocketService: WebSocketService
    private var isServiceBound = false
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
            if (isFilled() && isURLValid()) {
                addSite()
            } else {
                binding.apply {
                    siteNameLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        siteNameLayout.error = "Insert your site name"
                    }
                    siteUrlLayout.text.takeIf { it.isNullOrEmpty() }?.run {
                        siteUrlLayout.error = "Insert your site url"
                    }
                    siteUrlLayout.text.takeIf { !isURLValid() }?.run {
                        siteUrlLayout.error = "Insert valid site url"
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
        val serviceIntent = Intent(requireContext(), WebSocketService::class.java)
        requireContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

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
        appKey = binding.appKeyLayout.text.toString()
        appSecret = binding.appSecretLayout.text.toString()
        viewModel.setBaseUrl(siteUrl)
        viewModel.getToken(appKey, appSecret).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    token += data.data.data?.bearerToken.toString()
                    val site = Sites(
                        siteName,
                        siteUrl,
                    )
                    lifecycleScope.launch {
                        viewModel.insertSites(site)
                        viewModel.saveEncrypted(appKey, appSecret, token)
                        val list = listOf(appKey, appSecret, token)
                        viewModel.saveListEncrypted(siteName, list)
                        viewModel.getListEncrypted(siteName)
                        broadcast(appKey) // ambil data
                        val dialog =
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        dialog.apply {
                            titleText = "Successful!"
                            contentText = getString(R.string.toast_successful)
                            setConfirmClickListener {
                                it.dismiss()
                                toList()
                            }
                                .show()
                        }
                    }
                }

                is Event.Error -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(data.error)
                        .show()
                    Log.d("Calling error : ", data.error.toString())
                }

                else -> {
                    Log.d("Event ", data.toString())
                }
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as WebSocketService.LocalBinder
            webSocketService = binder.getService()
            isServiceBound = true
            Log.d("Testing", "Service terikat")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            Log.d("Testing", "Service terputus")
        }
    }

    private fun broadcast(channelKey: String) {
        Log.d("Testing","Hallo")
        val intent = Intent(context, MainActivity::class.java)
        intent.action = "ACTION_ADD_CHANNEL"
        requireContext().sendBroadcast(intent)
        val dataHolder : DataHolder by inject()
        dataHolder.channelKey = channelKey
        webSocketService.joinChannel()
    }


    private fun isURLValid(): Boolean {
        val baseUrl = binding.siteUrlLayout.text.toString().trim()
        return try {
            val url = URL(baseUrl)
            url.protocol == "http" || url.protocol == "https"
        } catch (e: MalformedURLException) {
            false
        }
    }
}