package com.bangkit.cloudraya.ui.confirmation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.cloudraya.R
import com.bangkit.cloudraya.databinding.FragmentConfirmationBinding
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.model.local.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Random
import java.util.concurrent.Executor

class ConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentConfirmationBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val viewModel: ConfirmationActivityViewModel by viewModel()
    private var msgBody: String = "Action Confirmation"
    private var token: String = "Bearer "
    private var request: String = ""
    private var siteUrl: String = ""
    private var vmId: String = ""
    private val dataHolder: DataHolder by inject()
    private lateinit var pDialog: SweetAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    interface ConfirmationFragmentListener {
        fun navigateToConfirmationFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hasBiometric()
        dataHolder.isPressed = true
        binding.btnHome.setOnClickListener {
//            val toHome = ConfirmationFragmentDirections
//            findNavController().navigate(toHome)

        }
    }

    private fun vmAction() {
        token += dataHolder.token
        request += dataHolder.request
        siteUrl += dataHolder.siteUrl
        vmId += dataHolder.vmId
        val title = dataHolder.title
        msgBody = dataHolder.msgBody
        dataHolder.action = ""
        viewModel.setBaseUrl(siteUrl)
        viewModel.vmAction(token, vmId.toInt(), request)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Event.Success -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successful!")
                            .setContentText(result.data.message)
                            .setConfirmClickListener {
                                toDetail()
                                it.dismissWithAnimation()
                            }
                            .show()
                        runBlocking {
                            delay(Toast.LENGTH_LONG.toLong())
                        }
                    }

                    is Event.Error -> {
                        pDialog.dismiss()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(result.error)
                            .show()
                    }

                    is Event.Loading -> {
                        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.titleText = "Loading"
                        pDialog.setCancelable(false)
                        pDialog.show()
                    }
                }
            }
    }

    private fun userAuth() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val userCancelled = errorCode == BiometricPrompt.ERROR_USER_CANCELED
                    if (userCancelled) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.action_denied),
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.action_denied),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    binding.tvMessage.text = getString(R.string.action_denied)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    sendResponseToFirebase()
                    vmAction()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.action_accepted),
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.tvMessage.text = getString(R.string.action_accepted)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(binding.root, "Authentication Failed!", Snackbar.LENGTH_LONG)
                        .show()
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(msgBody)
            .setDescription("Scan to accept the action!")
            .setNegativeButtonText("Deny")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun hasBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                userAuth()
            }

            else -> {
                val dialog = SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                dialog.titleText = msgBody
                dialog.contentText = "Confirm to accept the action"
                dialog.confirmText = "Accept"
                dialog.cancelText = "Deny"
                dialog.setConfirmClickListener {
                    sendResponseToFirebase()
                    vmAction()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.action_accepted),
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.tvMessage.text = getString(R.string.action_accepted)
                }
                dialog.setCancelClickListener {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.action_denied),
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.tvMessage.text = getString(R.string.action_denied)
                    dialog.dismissWithAnimation()
                }
                dialog.show()
            }
        }
    }

    private fun sendResponseToFirebase() {
        FirebaseApp.initializeApp(requireContext())

        val firebaseMessaging = FirebaseMessaging.getInstance()

        val data = hashMapOf(
            "response" to "fingerprint_success"
        )

        firebaseMessaging.send(
            RemoteMessage.Builder("1044426049361@gcm.googleapis.com")
                .setMessageId(Random().nextInt(1000000).toString())
                .setData(data)
                .build()
        )
    }

    private fun toDetail() {
        val toDetail = ConfirmationFragmentDirections.actionConfirmationFragmentToFragmentDetailVM(
            null,
            siteUrl,
            vmId
        )
        findNavController().navigate(toDetail)
    }

}