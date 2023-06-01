package com.bangkit.cloudraya.ui.siteAdd

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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.database.Sites
import com.bangkit.cloudraya.databinding.FragmentSiteAddBinding
import com.bangkit.cloudraya.model.local.Event
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.MalformedURLException
import java.net.URL

class FragmentSiteAdd : Fragment() {
    private lateinit var binding: FragmentSiteAddBinding
    private var token: String = "Bearer "
    private val viewModel: SiteAddViewModel by viewModel()
    private lateinit var databaseReference: DatabaseReference
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
        val appKey = binding.appKeyLayout.text.toString()
        val appSecret = binding.appSecretLayout.text.toString()

        val request = JsonObject().apply {
            addProperty("app_key", appKey)
            addProperty("secret_key", appSecret)
        }
        viewModel.setBaseUrl(siteUrl)
        viewModel.getToken(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    token += data.data.data?.bearerToken.toString()
                    val site = Sites(
                        siteName,
                        siteUrl,
                    )
                    val fcmToken = viewModel.getFCMToken() ?: ""
                    lifecycleScope.launch {
                        viewModel.insertSites(site)
                        viewModel.saveEncrypted(appKey, appSecret, token)
                        val list = listOf(appKey, appSecret, token)
                        viewModel.saveListEncrypted(siteName, list)
                        viewModel.getListEncrypted(siteName)
                        sendFCMToken(appKey, appSecret, fcmToken)
                        val dialog = SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
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

    private fun sendFCMToken(appKey: String, appSecret: String, fcmToken: String){
        databaseReference = FirebaseDatabase.getInstance("https://mobile-notification-90a3a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(appKey)
        val hashMap:HashMap<String, String?> = HashMap()
        hashMap["app_key"] = appKey
        hashMap["app_secret"] = appSecret
        hashMap["fcm_token"] = fcmToken
        databaseReference.setValue(hashMap)
            .addOnFailureListener {
                Log.d("RTDB Failure", "${it.message}  ${it.cause}")
            }
            .addOnSuccessListener {
                Log.d("RTDB Success", hashMap.toString())
            }
            .addOnCanceledListener {
                Log.d("RTDB Cancelled", "true")
            }
        Log.d("FCM Hash", hashMap.toString())
    }

    private fun isURLValid():Boolean{
        val baseUrl = binding.siteUrlLayout.text.toString().trim()
        return try {
            val url = URL(baseUrl)
            url.protocol == "http" || url.protocol == "https"
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun callToast() {
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val layout: View = inflater.inflate(
            R.layout.success_toast,
            requireActivity().findViewById(R.id.toast_successful)
        )
        val toast = Toast(requireContext())
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}